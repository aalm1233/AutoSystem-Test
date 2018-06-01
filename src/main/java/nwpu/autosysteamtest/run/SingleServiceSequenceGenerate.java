package nwpu.autosysteamtest.run;

import nwpu.autosysteamtest.entity.Service;
/**
 * 
 * @author Dengtong
 * @version 1.0,28/01/2018
 */
public class SingleServiceSequenceGenerate implements Runnable{
    Service service;
    String fileName;
    String path;
    public SingleServiceSequenceGenerate(Service service,String fileName,String path){
        this.service = service;
        this.fileName = fileName;
        this.path = path;
    }
    @Override
    public void run(){
    	long startTime = System.currentTimeMillis();
		System.out.println("Service : "+service.getId()+" Script generation start");
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
        long endTime = System.currentTimeMillis();
        System.out.println("Service : "+service.getId()+" Script generation finished.run with "+(endTime - startTime)+" ms");
    }
}