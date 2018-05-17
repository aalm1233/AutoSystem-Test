package nwpu.autosysteamtest;

import nwpu.autosysteamtest.enity.Service;

public class SingleServiceSequenceGenerate implements Runnable{
    Service service;
    String fileName;
    String path;
    public SingleServiceSequenceGenerate(Service service,String fileName,String path){
        this.service = service;
        this.fileName = fileName;
        this.path = path;
    }
    public void run(){
        GeneratingOperationSequence operationSequence = new GeneratingOperationSequence(service, fileName, path);
        try {
            operationSequence.generatingOperationSequenceFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GeneratingInterfaceSequence generatingInterfaceSequence = new GeneratingInterfaceSequence(service, fileName, path);
        try {
            generatingInterfaceSequence.generatingInterfaceSequenceFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}