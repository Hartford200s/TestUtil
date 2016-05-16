package action.subPackage1;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import anno.Mark;

public class SubactionClass2 {
	
	@Mark
	@RequestMapping(method = RequestMethod.GET)
	public void SubactionGet(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {
		
	}
	
	@Mark
	@RequestMapping(method = RequestMethod.POST)
	public void SubactionPost(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {
		
	}
	
	@Mark
	@RequestMapping()
	public void SubactionPost3(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {
		
	}
}
