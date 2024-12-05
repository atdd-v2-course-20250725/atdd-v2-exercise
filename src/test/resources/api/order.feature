# language: zh-CN
@api-login
功能: 订单

  场景: 订单列表
    假如存在"订单":
      | code  | productName | total | status        |
      | SN001 | 电脑          | 19999 | toBeDelivered |
    当GET "/orders"
    那么response should be:
    """
    body.json= [{
        code: "SN001"
        productName: "电脑"
        total: 19999
        status: "toBeDelivered"
    }]
    """
