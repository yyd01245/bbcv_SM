package prod.nebula.service.socket.center;


public class VodKeyCtrlReq {

	private int dev_type =1001;     
	private int sequence_num;  
	private int key_value; 
	private int key_status;
	
	public VodKeyCtrlReq(int sequence_num, int key_value,int key_status ) {
		super();
		this.sequence_num = sequence_num;
		this.key_value = key_value;
		this.key_status = key_status;
	}
	
	public int getDev_type() {
		return dev_type;
	}
	public void setDev_type(int dev_type) {
		this.dev_type = dev_type;
	}
	public int getSequence_num() {
		return sequence_num;
	}
	public void setSequence_num(int sequence_num) {
		this.sequence_num = sequence_num;
	}
	public int getKey_value() {
		return key_value;
	}
	public void setKey_value(int key_value) {
		this.key_value = key_value;
	}
	public int getKey_status() {
		return key_status;
	}
	public void setKey_status(int key_status) {
		this.key_status = key_status;
	}
}
