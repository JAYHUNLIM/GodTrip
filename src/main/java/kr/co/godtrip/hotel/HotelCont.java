package kr.co.godtrip.hotel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/hotel")
public class HotelCont {

	public HotelCont() {
		System.out.println("------HotelCont() 객체 생성됨");
	}
	@Autowired
	HotelDAO hotelDao;
	

	
 
	 @GetMapping("/hoteldetail")
	    public ModelAndView list2(HttpServletRequest req) {
	 		String hotel_code=req.getParameter("hotel_code");
	 		String departure_Date=req.getParameter("departure_Date");
	 		String arrival_Date=req.getParameter("arrival_Date");
	        ModelAndView mav=new ModelAndView();
	        mav.setViewName("hotel/hoteldetail");
	        mav.addObject("list",hotelDao.list2(hotel_code));//DB에서 where 칼럼명  
	        mav.addObject("departure_Date",departure_Date);
	        mav.addObject("arrival_Date",arrival_Date);
	        return mav;
	 }//list() end
	
	
	
	// @RequestMapping()
	 // 부모페이지 .jsp에서 <a href="/hotelList?area_code=여기에클릭한지역코드출력"></a>
	
	 	//호텔정보 가져오기 + 지역정보(사용자 기준)
	 	@GetMapping("/hotelList")
	 	public ModelAndView list(HttpServletRequest req) {
		 //사용자가 검색한 검색어(hotleList에서 넘어옴)
		 String hotel_Name = req.getParameter("hotel_Name");
		 //사용자가 선택한 타입 (hotleList에서 넘어옴)
		 String hotel_Type = req.getParameter("hotel_Type");
		 //사용자가 선택한 출발일 (transRsvInsert에서 넘어옴)
		 String departure_Date=req.getParameter("departure_Date");
		 //사용자가 선택한 도착일 (transRsvInsert에서 넘어옴)
		 String arrival_Date=req.getParameter("arrival_Date");
		 //사용자가 선택한 지역    (transRsvInsert에서 넘어옴)
		 String area_code=req.getParameter("area_code");
		 String area_name="";
		 	if(area_code.equals("G0001")) {
		    	area_name="제주도";
		    }else if(area_code.equals("G0002")) {
		    	area_name="서울";
		    }else if(area_code.equals("G0003")) {
		    	area_name="인천";
		    }else if(area_code.equals("G0004")) {
		    	area_name="수원";
		    }else if(area_code.equals("G0005")) {
		    	area_name="가평";
		    }else if(area_code.equals("G0006")) {
		    	area_name="강릉";
		    }else if(area_code.equals("G0007")) {
		    	area_name="춘천";
		    }
	        ModelAndView mav=new ModelAndView();
	        mav.setViewName("hotel/hotelList");

	        Map<String, Object> map=new HashMap<>();
	        map.put("area_code", area_code);
	        map.put("hotel_Name", hotel_Name);
	        map.put("hotel_Type", hotel_Type);

	        int totalRowCount=hotelDao.totalRowCount(map); //총 글갯수
	        
	        
	        //페이징
	        int numPerPage   = 5;    // 한 페이지당 레코드 갯수
	        int pagePerBlock = 5;   // 페이지 리스트
	        
	        String pageNum=req.getParameter("pageNum");
	        if(pageNum==null){
	              pageNum="1";
	        }
	        
	        int currentPage=Integer.parseInt(pageNum);
	        //페이지에 출력할 수
	        int startRow   =(currentPage-1)*numPerPage;  //가져올 데이터의 초기 위치값		
	        int endRow     =numPerPage;					 //가져올 데이터 양
	        
	        //페이지 수
	        double totcnt = (double)totalRowCount/numPerPage;
	        int totalPage = (int)Math.ceil(totcnt);
	        
	        double d_page = (double)currentPage/pagePerBlock;
	        int Pages     = (int)Math.ceil(d_page)-1;
	        int startPage = Pages*pagePerBlock;
	        int endPage   = startPage+pagePerBlock+1;
	      
	        
	       
	        map.put("startRow", startRow);
	        map.put("endRow", endRow);
	        map.put("area_code", area_code);
	        map.put("hotel_Name", hotel_Name);
	        map.put("hotel_Type", hotel_Type);
	        List list=null;      
	        if(totalRowCount>0){            
	        	list=hotelDao.list(map);
	        } else {            
	            list=Collections.EMPTY_LIST;            
	        }//if end
	        
	       System.out.println(list);
	        //페이징 정보 넘기기
	        mav.addObject("pageNum", currentPage);
	        mav.addObject("count", totalRowCount);
	        mav.addObject("totalPage", totalPage);
	        mav.addObject("startPage", startPage);
	        mav.addObject("endPage", endPage);
	        
	        
	        //숙박 정보 넘기기
	        mav.addObject("departure_Date",departure_Date);
	        mav.addObject("arrival_Date",arrival_Date);
	        mav.addObject("list", list);
            mav.addObject("hotel_Name", hotel_Name);
            mav.addObject("hotel_Type", hotel_Type);
	        mav.addObject("area_code", area_code);
	        mav.addObject("area_name",area_name);
	        return mav;
	 }//list() end
	 
}//class() end
