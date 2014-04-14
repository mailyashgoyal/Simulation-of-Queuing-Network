

public class EventList{
	public int event_count;
	public node head;
	public node p;
	 EventList()
    {
        event_count = 1;
		  
	 }

	EventList(Event x)
	{
		head= new node(x,null);
		p=head;
	}
	Event getEvent()
	{
		//for(p=start;p!=null;p=p.next)
		{
		return head.data;
		}
	}
	void printlist()
	{
		for(p=head;p!=null;p=p.next)
		System.out.println(p.data.type);
		System.out.println("done");
	}


	void insert(Event x)	
	{
		if(head.data.time>x.time)
		head= new node(x,head);
		else{
		node p=head;
		
		while(p.next!=null)
		{
			if(p.next.data.time>x.time)
			break;
			p=p.next;
		}
		p.next= new node(x,p.next);
		}
		 event_count++;
	}
	public void removeFront( )
    {
    	if(!(event_count == 0))
		{    
		  node p = head;
		 
		  head = p.next;
        event_count--;
      }
	 else
	 System.out.println("No Items to remove");
	 }

}
class node{
	public Event data;
	public  node next;
	public node(Event data, node next)
	{
		this.data=data;
		this.next=next;
	}
}
class Event{
	double time;
	int type;
	 Event(double time, int type)
	{
	this.time=time;
	this.type=type;
	}
}
