/**  
 * ����ƣ�Page 
 * �������� 
 * �����ˣ�PengFei   
 * ����ʱ�䣺2014-9-28 ����06:24:49 
 */
package pojo;

/**   
 * Page   
 * 分页 封装类   
 * @作者 PengFei   
 * 日期2014-9-28 06:24:49   
 *
 * @version    
 *    
 */
public class Page {
      
	 private int totalRecords;    //总记录数
	 private int totalPages;     //总页数
	 private int perPageRecords =10;  //每页条数
	 private int currentPage;   //当前页
	 
	 
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPerPageRecords() {
		return perPageRecords;
	}
	public void setPerPageRecords(int perPageRecords) {
		this.perPageRecords = perPageRecords;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	 
	 
	 
}
