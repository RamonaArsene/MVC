package ro.teamnet.zth;

import ro.teamnet.zth.api.annotations.Controller;
import ro.teamnet.zth.api.annotations.RequestMethod;
import ro.teamnet.zth.appl.controller.DepartmentController;
import ro.teamnet.zth.appl.controller.EmployeeController;
import ro.teamnet.zth.fmk.MethodAttributes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */
@WebServlet(name = "MyDispatcherServlet")
public class MyDispatcherServlet extends HttpServlet {
    
    Map<String, MethodAttributes> allowedMethods = new HashMap<String, MethodAttributes>();

    public void init(){
        MethodAttributes methodAttributes = new MethodAttributes();
        Class clazz = EmployeeController.class;
        methodAttributes.setControllerClass("EmployeeController");
        Controller controller = (Controller) clazz.getAnnotation(Controller.class);
        String url = controller.urlPath();
        for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            url = controller.urlPath() + method.getAnnotation(RequestMethod.class).urlPath() + method.getAnnotation(RequestMethod.class).methodType();
            methodAttributes.setControllerClass("EmployeeController");
            methodAttributes.setMethodType(method.getAnnotation(RequestMethod.class).methodType());
            allowedMethods.put(url, methodAttributes);
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.dispatchReply(request, response, "POST");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.dispatchReply(request, response, "GET");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
        this.dispatchReply(req, resp, "PUT");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
        this.dispatchReply(req, resp,"DELETE");
    }

    public void dispatchReply(HttpServletRequest request, HttpServletResponse response, String method) throws IOException {
        try{
            Object resultToDisplay = dispatch(request, response, method);
            reply(response, resultToDisplay);
        }catch(Exception e){
            sendExceptionError(response,"Eroare");
        }
        
    }

    private void reply(HttpServletResponse response, Object resultToDisplay) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {
       response.getWriter().println(resultToDisplay.toString());

    }

    private Object dispatch(HttpServletRequest request, HttpServletResponse response, String method) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException {
//
//        Object results = null;
//        String path = request.getPathInfo();
//
//        if(path.equals("/employees"))
//            results = "Employees";
//        else
//            if(path.equals("/employees/all"))
//            {
//                results = new EmployeeController().getAllEmployees();
//            }
//            else
//                if(path.equals("employees/one")){
//                results = new EmployeeController().getAllEmployees();
//            }
//            else
//                if(path.equals("/departments/all"))
//                {
//                    results = new DepartmentController().getAllDepartments();
//                }
//                else
//                if(path.equals("/departments/one"))
//                {
//                    results = new DepartmentController().getOneDepartment();
//                }

        String key = request.getPathInfo() + method;
        String[] path = request.getPathInfo().split("/");
        MethodAttributes methodAttributes = allowedMethods.get(key);
        Object controller = Class.forName(methodAttributes.getControllerClass()).newInstance();
       // return controller.getClass().getName();
        java.lang.reflect.Method [] classMethods = controller.getClass().getDeclaredMethods();

        for (Method classMethod : classMethods) {
            if(classMethod.getName().equals(methodAttributes.getMethodName())){
                response.getWriter().println(controller.getClass().getName());
                return controller.getClass().getMethod(methodAttributes.getMethodName()).invoke(controller);
            }

        }

        return null;
    }

    public void sendExceptionError (HttpServletResponse response, String err) throws IOException {
        response.getWriter().write(err);
    }
}
