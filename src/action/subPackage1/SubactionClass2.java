package action.subPackage1;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import anno.Mark;

public class SubactionClass2 {
	
	@Mark(actionURL = "goSearch.action", requestMethod = RequestMethod.POST)
	@RequestMapping(method = RequestMethod.GET)
	public void SubactionGet(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {}
	
	@Mark(actionURL = "SubactionPost.action", requestMethod = RequestMethod.POST)
	@RequestMapping(method = RequestMethod.POST)
	public void SubactionPost(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {}
	
	@Mark(actionURL = "SubactionPost3.action", requestMethod = RequestMethod.POST)
	@RequestMapping()
	public void SubactionPost3(int i, String s, @PathVariable String userId, @RequestParam(value="name")String name) {}
}
