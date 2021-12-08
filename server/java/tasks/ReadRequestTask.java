package tasks;

import common.Request;
import requestReader.RequestReader;

import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;

public class ReadRequestTask extends RecursiveTask<Request> {
    private RequestReader requestReader;
    public ReadRequestTask(RequestReader requestReader){
        this.requestReader = requestReader;
    }

    @Override
    public Request compute(){
        Request request = requestReader.getRequest();
        return request;
    }

}
