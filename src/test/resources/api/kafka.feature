# language: zh-CN
@api-login
功能: Kafka

  场景: send data to kafka
    假如清空队列"dotnet.message"
    当POST "/kafka":
    """
    {
      "name": "John"
    }
    """
    那么队列应为:
    """
    queue[dotnet.message].json[]= [{Name: John}]
    """

  场景: receive data from kafka
    当向队列"dotnet.data"写入:
    """
    {
      "UserName": "Tom",
      "Password": "123456"
    }
    """
    那么所有"用户"应为:
    """
    : [*, {
      userName: Tom
      password: '123456'
    }]
    """
