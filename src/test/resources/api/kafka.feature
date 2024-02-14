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
    那么队列"dotnet.message"应为:
    """
    [{
      "name": "John"
    }]
    """

