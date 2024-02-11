package com.odde.atddv2;

import com.odde.atddv2.grpc.GreeterGrpc;
import com.odde.atddv2.grpc.HelloReply;
import com.odde.atddv2.grpc.HelloRequest;
import io.grpc.stub.StreamObserver;

public class GreetingImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
