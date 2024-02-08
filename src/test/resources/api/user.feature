# language: zh-CN
功能: 用户

  场景: 用户登录成功
    假如存在"用户":
      | userName | password |
      | tom      | 123      |
    当POST "../users/login":
    """
    {
      "userName": "tom",
      "password": "123"
    }
    """
    那么response should be:
    """
    : {
      code: 200
      headers: {
        token.base64.json: {
          user: tom
          now: *
        }
      }
    }
    """

  场景: 订单列表 - 401 without token
    假如存在"订单":
      | code  | productName | total | status        |
      | SN001 | 电脑          | 19999 | toBeDelivered |
    当GET "/orders"
    那么response should be:
    """
    code= 401
    """

  场景: 订单列表 - 401 with invalid token
    假如存在"订单":
      | code  | productName | total | status        |
      | SN001 | 电脑          | 19999 | toBeDelivered |
    假如token为"invalid"
    当GET "/orders"
    那么response should be:
    """
    code= 401
    """
