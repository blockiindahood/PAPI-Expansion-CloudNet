package de.blocki.papi.expansion.services;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.wrapper.Wrapper;

import java.util.ArrayList;
import java.util.Collection;

public class CloudServers {

    public Collection<ServiceInfoSnapshot> getServices(){
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices();
    }

    public ServiceInfoSnapshot getServiceByName(String serviceName){
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(serviceName);
    }

    public ServiceId getServiceId() {
        return getCurrentServiceInfoSnapshot().getServiceId();
    }

    public ServiceInfoSnapshot getCurrentServiceInfoSnapshot() {
        return Wrapper.getInstance().getCurrentServiceInfoSnapshot();
    }

    public boolean serviceRunning(String serviceName){
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServiceByName(serviceName).isConnected();
    }

    public Collection<ServiceTask> getTasks(){
        return CloudNetDriver.getInstance().getServiceTaskProvider().getPermanentServiceTasks();
    }

    public ServiceTask getTaskByName(String taskName){
        return CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(taskName);
    }

    public ArrayList<ServiceInfoSnapshot> getServicesByTask(ServiceTask task){
        return new ArrayList(CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices(task.getName()));
    }

    public void createCloudServiceByTask(String taskname) {
        if (CloudNetDriver.getInstance().getServiceTaskProvider().isServiceTaskPresent(taskname)) {
            ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(taskname); //getDef ServiceTask instance
            ServiceInfoSnapshot serviceInfoSnapshot = CloudNetDriver.getInstance().getCloudServiceFactory().createCloudService(serviceTask); //Creates a service on cluster and returns the initial snapshot
            if (serviceInfoSnapshot != null) {
                serviceInfoSnapshot.provider().start(); //Starting service
            }
        }
    }

    public void startCloudService(ServiceInfoSnapshot sis) //start the cloud service. if the configuration for the service autoDeleteOnStop not enabled. You can restart the service
    {
        CloudNetDriver.getInstance().getCloudServiceProvider(sis).start();
    }

    public void stopCloudService(ServiceInfoSnapshot sis) //stop the cloud service. if the configuration for the service autoDeleteOnStop not enabled. You can restart the service
    {
        CloudNetDriver.getInstance().getCloudServiceProvider(sis).stop();
    }

    public void stopAndDeleteService(ServiceInfoSnapshot sis) //stops and deletes the service gracefully
    {
        CloudNetDriver.getInstance().getCloudServiceProvider(sis).delete();
    }
}
