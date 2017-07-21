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
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ramona.Arsene on 7/20/2017.
 */
@WebServlet(name = "MyDispatcherServlet")
public class MyDispatcherServlet extends HttpServlet {
    
    Map<String, MethodAttributes> allowedMethods = new HashMap<String, MethodAttributes>();

    public void init( ) {
        MethodAttributes methodAttributes = new MethodAttributes();
        Class clazz = DepartmentController.class;
        methodAttributes.setControllerClass("DepartmentController");
        Controller controller = (Controller) clazz.getAnnotation(Controller.class);
        //resp.getWriter().println("init " + controller.urlPath());
        String url;
        for (java.lang.reflect.Method method : DepartmentController.class.getDeclaredMethods()) {
            url = controller.urlPath() + method.getAnnotation(RequestMethod.class).urlPath().toString() + method.getAnnotation(RequestMethod.class).methodType().toString();
            //resp.getWriter().println("url init: " + url);
            methodAttributes.setControllerClass("DepartmentController");
            methodAttributes.setMethodType(method.getAnnotation(RequestMethod.class).methodType().toString());
            methodAttributes.setMethodName(method.getName());
            //resp.getWriter().println("method name:" + methodAttributes.getMethodName());
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

    private Object dispatch(HttpServletRequest request, HttpServletResponse response, String method) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException, ServletException {
        Object results = null;
        //init(response);
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
        response.getWriter().println("key" + key);
        MethodAttributes methodAttributes = allowedMethods.get(key);
        response.getWriter().println("key value" + allowedMethods.get(key));


        response.getWriter().println(methodAttributes.getMethodName());
        Object controller = Class.forName(methodAttributes.getControllerClass()).newInstance();
        results = controller.getClass().getDeclaredMethod(methodAttributes.getMethodName()).invoke(controller);
//
//        for (Method classMethod : classMethods) {
//            if(classMethod.getName().equals(methodAttributes.getMethodName())){
//                logHeader(controller.getClass().getMethod(methodAttributes.getMethodName()).toString());
//            }
//        }
        return results;
    }

    public void sendExceptionError (HttpServletResponse response, String err) throws IOException {

        logHeader(err);
        response.getWriter().write(err);
    }

    public static void logHeader(String headerName){

        try(RandomAccessFile randomAccessFile=new RandomAccessFile("D://out.log","rw")) {
            randomAccessFile.writeBytes( headerName + ":" +"\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
