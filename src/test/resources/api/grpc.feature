# language: zh-CN
@api-login
功能: Grpc

  场景: grpc
    并且存在grpc服务:
    """
    {}
    """
    当GET "/grpc"
    那么response should be:
    """
    body.string= 'Hello John'
    """
