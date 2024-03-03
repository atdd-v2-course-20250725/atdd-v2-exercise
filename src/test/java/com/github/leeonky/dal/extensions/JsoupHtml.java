package com.github.leeonky.dal.extensions;

import com.github.leeonky.dal.DAL;
import com.github.leeonky.dal.runtime.Extension;
import com.github.leeonky.dal.runtime.JavaClassPropertyAccessor;
import com.github.leeonky.dal.runtime.RuntimeContextBuilder;
import com.github.leeonky.util.BeanClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * 通过这个类 让DAL 可以通过 html.table 这样的表达式 把一个html字符串作为输入，然后进行 html table 的数据检查,
 * 只需要把这个类放在 com.github.leeonky.dal.extensions 下即可
 * <p>
 * 其实扩展 DAL 支持更多的数据格式的主要思路是通过扩展方法将被检查的具体java 数据实例包装成新的类型，
 * 例如本例的table方法返回一个HtmlDataTable对象，然后通过 registerPropertyAccessor 方法让 DAL
 * 以数据的视角访问被检查的业务数据
 */
public class JsoupHtml implements Extension {

    @Override
    public void extend(DAL dal) {
        RuntimeContextBuilder runtimeContextBuilder = dal.getRuntimeContextBuilder();
        runtimeContextBuilder
                // 再DAL中给String 扩展 html 方法，可以把String 转换为 html 对象
                .registerStaticMethodExtension(StaticMethods.class)

                // 告诉DAL 怎么以一种业务数据的视角处理 HtmlDataTable.HtmlDataRow 类型
                .registerPropertyAccessor(HtmlDataTable.HtmlDataRow.class, new JavaClassPropertyAccessor<>(
                        runtimeContextBuilder, BeanClass.create(HtmlDataTable.HtmlDataRow.class)) {
                    @Override
                    public Object getValue(HtmlDataTable.HtmlDataRow instance, Object name) {
                        try {
                            return super.getValue(instance, name);
                        } catch (Exception ignore) {
                            return ofNullable(instance.getCell((String) name)).orElseThrow(() ->
                                    new IllegalArgumentException("cell or property '" + name + "' not exist"));
                        }
                    }
                })
        ;

        // 再 DAL 中支持通过 : 来进行 html cell 中和 String 或 double 的断言比较
        runtimeContextBuilder.getConverter().addTypeConverter(Element.class, String.class, Element::text);
        runtimeContextBuilder.getConverter().addTypeConverter(Element.class, Double.class, element -> Double.parseDouble(element.text()));
    }

    public static class StaticMethods {

        // 在DAL中给String 扩展 html 方法，可以把String 转换为 Document 对象
        public static Document html(String html) {
            return Jsoup.parse(html);
        }

        // 再给 Document 对象 扩展一个table 方法，从 html 中 通过css select 一个 html table,
        public static HtmlDataTable table(Element element) {
            // 需要结合项目实际的 UI 框架渲染出的 HTML 实现查找界面元素的逻辑
            return new HtmlDataTable(element.select(".el-table").first());
        }
    }

    public static class HtmlDataTable extends ArrayList<HtmlDataTable.HtmlDataRow> {
        private final Element table;

        public HtmlDataTable(Element table) {
            this.table = table;
            // 需要结合项目实际的 UI 框架渲染出的 HTML 实现查找界面元素的逻辑
            for (Element row : table.select("tbody tr")) {
                add(new HtmlDataRow(row));
            }
        }

        public class HtmlDataRow {
            private final Element tr;
            private final List<String> headers;

            public HtmlDataRow(Element tr) {
                this.tr = tr;
                headers = table.select("th").stream().map(Element::text).collect(Collectors.toList());
            }

            public Object getCell(String name) {
                int index = headers.indexOf(name);
                if (index < 0)
                    throw new IllegalArgumentException(String.format("header <%s> not exist", name));
                return tr.children().get(index);
            }
        }
    }
}
