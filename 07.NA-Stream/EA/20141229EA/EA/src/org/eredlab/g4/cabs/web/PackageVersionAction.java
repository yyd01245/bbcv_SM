package org.eredlab.g4.cabs.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.eredlab.g4.cabs.service.PackageVersionService;
import org.eredlab.g4.ccl.datastructure.Dto;
import org.eredlab.g4.ccl.datastructure.impl.BaseDto;
import org.eredlab.g4.ccl.json.JsonHelper;
import org.eredlab.g4.rif.web.BaseAction;
import org.eredlab.g4.rif.web.CommonActionForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class PackageVersionAction extends BaseAction {

	
	private static Log log = LogFactory.getLog(PackageVersionAction.class);
	
	private PackageVersionService packageVersionService = (PackageVersionService) super.getService("packageVersionService");
	/**
	 * 页面初始化
	 * 
	 * @param
	 * @return
	 */
	public ActionForward pageInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("initView");
	}
	
	/**
	 * Web表单文件上传 单个/批量同理
	 * 
	 * @param
	 * @return
	 */
	public ActionForward doUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cForm = (CommonActionForm) form;
		// 单个文件,如果是多个就cForm.getFile2()....支持最多5个文件
		FormFile myFile = cForm.getFile1();
//		FormFile jarsh = cForm.getFile2();
//		String jarshName = jarsh.getFileName();
		// 获取web应用根路径,也可以直接指定服务器任意盘符路径
		String contextPath = getServlet().getServletContext().getRealPath("/");
		String context = request.getContextPath();
		String savePath = contextPath.substring(0, contextPath.indexOf(context.substring(1)))  + "ROOT/upload/";
		System.out.println("savePath==========>"+savePath);
		
		//String savePath = "d:/upload/";
		// 检查路径是否存在,如果不存在则创建之
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 文件真实文件名
		String fileName = myFile.getFileName();
		// 我们一般会根据某种命名规则对其进行重命名
		// String fileName = ;
		File fileToCreate = new File(savePath, fileName);
		// 检查同名文件是否存在,不存在则将文件流写入文件磁盘系统
		if (!fileToCreate.exists()) {
			FileOutputStream os = new FileOutputStream(fileToCreate);
			os.write(myFile.getFileData());
			os.flush();
			os.close();
		} else {
			// 此路径下已存在同名文件,是否要覆盖或给客户端提示信息由你自己决定
			fileToCreate.delete();
			FileOutputStream os = new FileOutputStream(new File(savePath,fileName));
			os.write(myFile.getFileData());
			os.flush();
			os.close();
		}
		// 我们通常还会把这个文件的相关信息持久化到数据库
		
		Dto inDto = cForm.getParamAsDto(request);
		inDto.put("jarname", fileName);
//		inDto.put("jarsh", jarshName);
		String ip = request.getLocalAddr();
		int port = request.getLocalPort();
		String versionName = "http://"+ip+":"+port+"/upload/"+fileName;
		Integer count = (Integer)crsmReader.queryForObject("PackageVersion.reTarCount",inDto);//jar包重名校验
		inDto.put("count", count);
		Dto outDto = packageVersionService.insertPackage(inDto,versionName);
		String jsonString = JsonHelper.encodeObject2Json(outDto);
		write(jsonString, response);
		setOkTipMsg("文件上传成功", response);
		
		return mapping.findForward(null);
	}
	
	/**
	 * 更新升级包信息
	 * 同时更新升级包对应机器状态，设置为可升级状况
	 * @param
	 * @return
	 */
	public ActionForward updatePackage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm cForm = (CommonActionForm) form;
		// 单个文件,如果是多个就cForm.getFile2()....支持最多5个文件
		FormFile myFile = cForm.getFile2();
//		FormFile jarsh = cForm.getFile2();
//		String jarshName = jarsh.getFileName();
		// 获取web应用根路径,也可以直接指定服务器任意盘符路径
		String contextPath = getServlet().getServletContext().getRealPath("/");
		String context = request.getContextPath();
		String savePath = contextPath.substring(0, contextPath.indexOf(context.substring(1)))  + "ROOT/upload/";
		System.out.println("savePath==========>"+savePath);
		Dto inDto = cForm.getParamAsDto(request);
		Dto versionDto = (Dto) crsmReader.queryForObject("PackageVersion.queryFileName",inDto);
		String filename = versionDto.getAsString("tarname");
		//String savePath = "d:/upload/";
		// 检查路径是否存在,如果不存在则创建之
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		// 文件真实文件名
		String fileName = myFile.getFileName();
		if(fileName==""||"".equals(fileName)){
			fileName= filename;
		}else{
			fileName=fileName;
			// 我们一般会根据某种命名规则对其进行重命名
			// String fileName = ;
			File fileToCreate = new File(savePath, fileName);
			// 检查同名文件是否存在,不存在则将文件流写入文件磁盘系统
			if (!fileToCreate.exists()) {
				FileOutputStream os = new FileOutputStream(fileToCreate);
				os.write(myFile.getFileData());
				os.flush();
				os.close();
			} else {
				// 此路径下已存在同名文件,是否要覆盖或给客户端提示信息由你自己决定
				fileToCreate.delete();
				FileOutputStream os = new FileOutputStream(new File(savePath,fileName));
				os.write(myFile.getFileData());
				os.flush();
				os.close();
			}
		}
	
		// 我们通常还会把这个文件的相关信息持久化到数据库
		
		inDto.put("tarname", fileName);
//		inDto.put("tarsh", jarshName);
		String ip = request.getLocalAddr();
		int port = request.getLocalPort();
		String versionName;
		if(fileName==""||"".equals(fileName)){
			versionName="";
		}else{
			versionName= "http://"+ip+":"+port+"/upload/"+fileName;
		}		 
		Integer count = (Integer)crsmReader.queryForObject("PackageVersion.reTarCount",inDto);//jar包重名校验
		inDto.put("count", count);
		inDto.put("version_addr", versionName);
		//更新升级包信息
		Dto outDto = packageVersionService.updatePackage(inDto);
		String success = outDto.getAsString("success");
		if(success=="true"||"true".equals(success)){
			//更新升级包对应机器状态，设置为可升级状况
//			packageVersionService.updatePackageTermRel(inDto);
			//插入升级事件  TODO
		}
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	/**
	 * 查询升级包信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryFileDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForPage("PackageVersion.getPackageVersionListForPage", inDto);
		Integer totalCount = (Integer)crsmReader.queryForObject("PackageVersion.getPackageVersionListForPageCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询升级包信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryPackageList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForPage("PackageVersion.getPackageVersionListByType", inDto);
		Integer total = (Integer)crsmReader.queryForObject("PackageVersion.getPackageVersionListCount", inDto);
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, total, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	public ActionForward queryPackageList1(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("PackageVersion.getPackageVersionListByType", inDto);
		int total = codeList.size() ;
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, total, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 删除升级包信息
	 * 
	 * @param
	 * @return
	 */
	public ActionForward delFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String strChecked = request.getParameter("strChecked");
		Dto inDto = new BaseDto();
		Dto outDto = new BaseDto();
		inDto.put("strChecked", strChecked);
		outDto = packageVersionService.deletePackageVersionItems(inDto);
		String retString = JsonHelper.encodeObject2Json(outDto);
		write(retString, response);
		return mapping.findForward(null);
	}
	
	/**
	 * 查询升级包编号ID
	 * 
	 * @param
	 * @return
	 */
	public ActionForward queryRecordIDs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CommonActionForm aForm = (CommonActionForm) form;
		Dto inDto = aForm.getParamAsDto(request);
		List codeList = crsmReader.queryForList("PackageVersion.getRecordIds", inDto);
		Integer totalCount = codeList.size();
		String jsonStrList = JsonHelper.encodeList2PageJson(codeList, totalCount, null);
		write(jsonStrList, response);
		return mapping.findForward(null);
	}
}
