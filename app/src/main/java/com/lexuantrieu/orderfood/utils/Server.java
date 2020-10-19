package com.lexuantrieu.orderfood.utils;

public class Server {
    private static String localhost = "lexuantrieu.000webhostapp.com";

    public static String urlImage = "http://"+localhost+"/dborderfoodapp/public/images/";

    public static String urlGetFood = "http://"+localhost+"/dborderfoodapp/getFood.php";
    public static String urlInsertOL = "http://"+localhost+"/dborderfoodapp/insertOrderList.php";
    public static String urlUpdateOL = "http://"+localhost+"/dborderfoodapp/updateOrderList.php";
    public static String urlUploadImage = "http://"+localhost+"/dborderfoodapp/uploadImage.php";
}
