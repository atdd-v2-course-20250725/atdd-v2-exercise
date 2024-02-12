package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.leeonky.cucumber.restful.RestfulStep;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.docstring.DocString;
import io.cucumber.java.Before;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.并且;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

public class ApiOrderSteps {

    @Autowired
    MockServer mockServer;

    @Value("${binstd-endpoint.key}")
    private String binstdAppKey;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RestfulStep restfulStep;

    @PostConstruct
    public void setBaseUrl() {
        restfulStep.setBaseUrl("http://localhost:10081/api");
    }

    @SneakyThrows
    @Before("@api-login")
    public void apiLogin() {
        User defaultUser = new User().setUserName("j").setPassword("j");
        userRepo.save(defaultUser);
        ObjectMapper objectMapper = new ObjectMapper();
        restfulStep.setBaseUrl("http://localhost:10081");
        restfulStep.post("/users/login", DocString.create(objectMapper.writeValueAsString(defaultUser)));
        String token = restfulStep.response("headers.token");
        restfulStep.header("token", token);
        restfulStep.setBaseUrl("http://localhost:10081/api");
    }

    @并且("存在快递单{string}的物流信息如下")
    public void 存在快递单的物流信息如下(String deliverNo, String json) {
        mockServer.getJson("/express/query", (request) -> request.withQueryStringParameter("appkey", binstdAppKey)
                .withQueryStringParameter("type", "auto")
                .withQueryStringParameter("number", deliverNo), json);
    }

    @假如("token为{string}")
    public void token为(String token) {
        restfulStep.header("token", token);
    }

    @SneakyThrows
    @并且("存在grpc服务:")
    public void 存在grpc服务(String body) {
        new Thread(ApiOrderSteps::runGrpc).start();
        TimeUnit.SECONDS.sleep(5);
//        runGrpc();
    }

    @SneakyThrows
    private static void runGrpc() {
        Server server = ServerBuilder
                .forPort(3254)
                .addService(new GreetingImpl())
                .addService(ProtoReflectionService.newInstance())
                .build();

        server.start();
        server.awaitTermination();
    }

    @SneakyThrows
    @并且("打桩grpc服务:")
    public void 打桩grpc服务(String grpcStub) {
        RestfulStep restfulStep = new RestfulStep();
        restfulStep.setBaseUrl("http://localhost:4771");
        restfulStep.delete("/api/stubs");
        restfulStep.post("/api/stubs", grpcStub);
    }
}
