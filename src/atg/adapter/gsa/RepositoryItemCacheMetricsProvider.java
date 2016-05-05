package atg.adapter.gsa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import atg.nucleus.GenericService;

public class RepositoryItemCacheMetricsProvider extends GenericService{
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private WriteLock writeLock = lock.writeLock();
    public void addRepositoryToMonitor(String repositoryPath){
    	try{
    	writeLock.lock();
    	getRepositoryRegistry().add(repositoryPath);
    	}finally{
    		writeLock.unlock();
    	}
    }

    public void removeRepositoryToMonitor(String repositoryPath){
    	try{
    	writeLock.lock();
    	getRepositoryRegistry().remove(repositoryPath);
    	}finally{
    		writeLock.unlock();
    	}
    }
    
    @Override
    public void doStartService() throws atg.nucleus.ServiceException {
    	super.doStartService();
    }
    
	private List<String> repositoryRegistry;
	public List<String> getRepositoryRegistry() {
		if(repositoryRegistry==null)
			repositoryRegistry = new ArrayList<String>();
		return repositoryRegistry;
	}
	
	public void setRepositoryRegistry(List<String> repositoryRegistry) {
		this.repositoryRegistry = repositoryRegistry;
	}

	public int getEntryCount(GSAItemDescriptor itemDescriptor) {
		return itemDescriptor.getItemCache().getEntryCount();
	}
	
	public int getHitCount(GSAItemDescriptor itemDescriptor) {
		return itemDescriptor.getItemCache().getHitCount();
	}
	
	public int getMissCount(GSAItemDescriptor itemDescriptor) {
		return itemDescriptor.getItemCache().getMissCount();
	}
	
	public int getItemCacheSize(GSAItemDescriptor itemDescriptor) {
		return itemDescriptor.getItemCache().getItemCacheSize();
	}
	
	public int getlocalEntryCount(GSAItemDescriptor itemDescriptor) {
		return itemDescriptor.getItemCache().getLocalEntryCount();
	}
	
	public float getHitVsMissRatio(GSAItemDescriptor itemDescriptor) {
		float ratio=0;
		int hitCount = getHitCount(itemDescriptor);
		int missCount = getMissCount(itemDescriptor);
		int sum =hitCount + missCount;
		if(sum!=0){
			ratio=(((float)hitCount/sum)*100);
		}
		return ratio;
	}
	
	
}


