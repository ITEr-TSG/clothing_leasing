package com.yj.clothing.controller;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yj.clothing.beans.Clothing;
import com.yj.clothing.service.ClothingService;
import com.yj.utils.AnalysisKeyWordsListUtils;
import com.yj.utils.ConstantUtils;
import com.yj.utils.Msg;
import com.yj.utils.UUIDUtil;
import com.yj.utils.UploadFileUtil;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 应建
 * @since 2019-12-24
 */
@Controller
@RequestMapping("/clothing")
public class ClothingController {
	
	@Autowired
	private ClothingService clothingSer;
	
	/**
	 * 批量隐藏
	 * */
	@RequestMapping(value="/hideClothingByIds",method=RequestMethod.POST)
	@ResponseBody
	public Msg hideClassifyByIds(@RequestBody ArrayList<Integer> list) {
		List<Clothing> cs = clothingSer.selectBatchIds(list);
		cs.forEach(clazz -> clazz.setClothingState("隐藏"));
		boolean b = clothingSer.updateBatchById(cs);
		if(!b) {
			return Msg.fail().add("msg","失败！");
		}
		return Msg.success().add("msg", "成功");
	}
	/**
	 * 批量展示
	 * */
	@RequestMapping(value="/showClothingByIds",method=RequestMethod.POST)
	@ResponseBody
	public Msg showClothingByIds(@RequestBody ArrayList<Integer> list) {
		List<Clothing> cs = clothingSer.selectBatchIds(list);
		cs.forEach(clazz -> clazz.setClothingState("展示"));
		boolean b = clothingSer.updateBatchById(cs);
		if(!b) {
			return Msg.fail().add("msg","失败！");
		}
		return Msg.success().add("msg", "成功");
	}
	
	/**
	 * 批量删除
	 * */
	@RequestMapping(value="/delClothingByIds",method=RequestMethod.POST)
	@ResponseBody
	public Msg delClassifyByIds(@RequestBody ArrayList<Integer> list) {
		boolean b = clothingSer.deleteBatchIds(list);
		if(!b) {
			return Msg.fail().add("msg","删除失败！");
		}
		return Msg.success().add("msg", "删除成功");
	}
	
	/**
	 * 得到展示的服装
	 * @return 
	 * @throws ParseException 
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/getShowClothingList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getShowClassifyList(@RequestBody Map<Object,Object> kwMap) throws ParseException {
		int page = (int) kwMap.get("page");
		int limit = (int) kwMap.get("limit");
		ArrayList<Map> arrayList = new ArrayList<>();
		arrayList = (ArrayList<Map>) kwMap.get("kwdata");
		AnalysisKeyWordsListUtils utils = new AnalysisKeyWordsListUtils();
		HashMap<String, Object> afterMap = utils.analysisKeyWordsList(arrayList);
		String name = (String) afterMap.get("classifyName");
		String clothingClassifyId = (String) afterMap.get("clothingClassifyId");
		int classifyId = Integer.parseInt(clothingClassifyId);
		String start_date = (String) afterMap.get("start_date");
		String end_date = (String) afterMap.get("end_date");
		EntityWrapper<Clothing> wrapper = new EntityWrapper<>();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		if(!name.equals("")) {
			wrapper.like("clothing_name", name).or().like("clothing_describe", name);
		}
		if(classifyId!=0) {
			wrapper.eq("clothing_classify_id", classifyId);
		}
		if(!start_date.equals("") && !end_date.equals("")) {
			Date startDate = format1.parse(start_date);
			Date endDate = format1.parse(end_date);
			wrapper.between("create_time", startDate, endDate);
		}
		wrapper.eq("clothing_state","展示").orderBy("clothing_id", false);
		Page<Map<String, Object>> mapsPage = clothingSer.selectMapsPage(new Page<>(page, limit), wrapper);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status",0);
		resultMap.put("message","所有展示的服装");
		resultMap.put("total",mapsPage.getTotal());
		resultMap.put("data",mapsPage.getRecords());
		return resultMap;
	}
	/**
	 * 得到隐藏的服装
	 * @return 
	 * @throws ParseException 
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="/getHideClothingList",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getHideClothingList(@RequestBody Map<Object,Object> kwMap) throws ParseException {
		int page = (int) kwMap.get("page");
		int limit = (int) kwMap.get("limit");
		ArrayList<Map> arrayList = new ArrayList<>();
		arrayList = (ArrayList<Map>) kwMap.get("kwdata");
		AnalysisKeyWordsListUtils utils = new AnalysisKeyWordsListUtils();
		HashMap<String, Object> afterMap = utils.analysisKeyWordsList(arrayList);
		String name = (String) afterMap.get("classifyName");
		String clothingClassifyId = (String) afterMap.get("clothingClassifyId");
		int classifyId = Integer.parseInt(clothingClassifyId);
		String start_date = (String) afterMap.get("start_date");
		String end_date = (String) afterMap.get("end_date");
		EntityWrapper<Clothing> wrapper = new EntityWrapper<>();
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		if(!name.equals("")) {
			wrapper.like("clothing_name", name).or().like("clothing_describe", name);
		}
		if(classifyId!=0) {
			wrapper.eq("clothing_classify_id", classifyId);
		}
		if(!start_date.equals("") && !end_date.equals("")) {
			Date startDate = format1.parse(start_date);
			Date endDate = format1.parse(end_date);
			wrapper.between("create_time", startDate, endDate);
		}
		wrapper.eq("clothing_state","隐藏").orderBy("clothing_id", false);
		Page<Map<String, Object>> mapsPage = clothingSer.selectMapsPage(new Page<>(page, limit), wrapper);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status",0);
		resultMap.put("message","所有隐藏的服装");
		resultMap.put("total",mapsPage.getTotal());
		resultMap.put("data",mapsPage.getRecords());
		return resultMap;
	}
	
	/**
	 * 添加服装
	 * */
	@RequestMapping(value="/addClothing",method=RequestMethod.POST)
	@ResponseBody
	public Msg addClassify(Clothing clo) {
		clo.setClothingIdent(UUIDUtil.createUUID());
		Integer classifyId = clo.getClothingClassifyId();
		if(classifyId==0) {
			return Msg.fail().add("msg", "请选择所属分类！");
		}
		boolean b = clothingSer.insert(clo);
		if(b) {
			return Msg.success().add("msg", "成功");
		}
		return Msg.fail().add("msg", "失败");
	}
	
	/**
	 * 修改服装信息
	 * */
	@RequestMapping(value="/editClothing",method=RequestMethod.POST)
	@ResponseBody
	public Msg editClothing(Clothing clo) {
		boolean b = clothingSer.updateById(clo);
		if(b) {
			return Msg.success().add("msg", "成功");
		}
		return Msg.fail().add("msg", "失败");
	}
	
	
	/**
	 * 上传图片
	 * @return 
	 * */
	@RequestMapping(value="/uploadClothingImg",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadClothingImg(@RequestParam("file") CommonsMultipartFile file) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UploadFileUtil util = new com.yj.utils.UploadFileUtil();
		String result = util.uploadImgFile(file, ConstantUtils.CLOTHING_IMGS);
		if(result.equals("error")) {
			resultMap.put("code", 1);
			resultMap.put("msg", "图片上传失败");
			return resultMap;	
		}
		String url = "/file/clothing_imgs/"+result;
		resultMap.put("code", 0);
		resultMap.put("msg", "图片上传成功");
		resultMap.put("data", url);
		return resultMap;
	}
	//=================页面跳转================
	//添加服装
	@RequestMapping("/toAddClothingPage")
	public String toAddClothingPage() {
		return "/clothing/add-clothing";
	}
	//展示服装
	@RequestMapping("/toShowClothingPage")
	public String toShowClothingPage() {
		return "/clothing/show-clothing";
	}
	//展示服装
	@RequestMapping("/toHideClothingPage")
	public String toHideClothingPage() {
		return "/clothing/hide-clothing";
	}
}
