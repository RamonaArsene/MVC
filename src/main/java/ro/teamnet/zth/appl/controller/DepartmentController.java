package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.Controller;
import ro.teamnet.zth.api.annotations.RequestMethod;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */
@Controller(urlPath = "/departments")
public class DepartmentController {

    @RequestMethod(urlPath = "/all", methodType = "GET")
    public String getAllDepartments(){
        return "allDepartments";
    }

    @RequestMethod(urlPath = "/one", methodType = "GET")
    public String getOneDepartment(){
        return "oneRandomDepartment";
    }
}
