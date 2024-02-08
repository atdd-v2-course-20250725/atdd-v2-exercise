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
        token: hello-world
      }
    }
    """
