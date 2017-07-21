package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.Controller;
import ro.teamnet.zth.api.annotations.RequestMethod;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */
@Controller(urlPath = "/employees")
public class EmployeeController {


    @RequestMethod(urlPath = "/all", methodType = "GET")
    public String getAllEmployees(){
        return "allEmployees";
    }

    @RequestMethod(urlPath = "/one", methodType = "GET")
    public String getOneEmployee(){
        return "oneRandomEmployee";
    }
}
