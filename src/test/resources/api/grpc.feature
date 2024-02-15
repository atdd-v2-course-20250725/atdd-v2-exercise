# language: zh-CN
@api-login
功能: Grpc

  场景: grpc-mock
    并且打桩grpc服务:
    """
    {
      "service":"Greeter",
      "method":"SayHello",
      "input":{
        "equals":{
          "name":"John"
        }
      },
      "output":{
        "data":{
          "message":"Hello GripMock"
        }
      }
    }
    """
    当GET "/grpc"
    那么response should be:
    """
    body.string= 'Hello GripMock'
    """
