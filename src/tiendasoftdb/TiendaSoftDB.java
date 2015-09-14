package tiendasoftdb;
import java.util.Scanner;
import java.lang.Math;
import java.sql.*;

public class TiendaSoftDB {
   int contUser;
   int flag=0;
   private String nombre;
   private double cantidad;
   private double ventasTotales;
   private double precio;
   private double ganancias;
   private double id;
   Scanner valor = new Scanner(System.in);
   private static boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
       }
   public void Menu(){
        System.out.println("Menu");
        System.out.println("1. Agregar Producto");
        System.out.println("2. Buscar Producto");
        System.out.println("3. Eliminar Producto");
        System.out.println("4. Mostrar Inventario");
        System.out.println("5. Realizar Venta");
        System.out.println("6. Mostar Ganancias totales");
        System.out.println("7. Salir");
     }
   public void agregar(){  
       String temp,temp2;
       int flag2=0,flag3=0;
             System.out.println("Ingrese el Nombre del producto: "); 
            nombre=valor.nextLine();
            do{
                System.out.println("Ingrese La cantidad del producto: ");
                temp=valor.nextLine();
                if (isNumeric(temp)){
                        flag3=1;
                        cantidad = Integer.parseInt(temp);
                }else System.out.println("Hay un error, por favor ingrese los datos de manera correcta ");
            }while(flag3!=1);
            do{
            System.out.println("Ingrese el precio : "); 
            temp2=valor.nextLine();
                if (isNumeric(temp2)){
                        flag2=1;
                        precio = Integer.parseInt(temp2);
                }else System.out.println("Hay un error, por favor ingrese los datos de manera correcta ");
            }while(flag2!=1);
    }
   public void venta(){
    double ventas;
    String temp;
    System.out.println("Ingrese la cantidad que quiere comprar: ");
    temp=valor.nextLine();
    if (isNumeric(temp)){
    ventas = Integer.parseInt(temp);
    cantidad=cantidad-ventas;
        if(cantidad<0){
            cantidad=cantidad+ventas;
            System.out.println("Esa cantidad de productos no hay disponibles en este momento");
        }else {
            flag=2;
            ventasTotales=ventasTotales+ventas;
            ganancias=ventasTotales*precio;
            System.out.println("Venta realizada");  
            System.out.println("  ");
        }
    }else{
       System.out.println("Verifique la cantidad ingresada"); 
       System.out.println("  ");
    }
}
    public static void main(String[] args) {
       int i,j,k,flag3=0;
       String opc;
       TiendaSoftDB datos = new TiendaSoftDB();
       String user="root";
       String password="stivinsondatos";
       String url= "jdbc:mysql://localhost/tiendasof";
       String url2= "jdbc:mysql://localhost/tiendamovil";
       String user2="stivinson";
       String password2="moviles123";
       String url3= "jdbc:mysql://db4free.net/tiendamovil";
       String nombre;
       String cantidad;
       Scanner valor = new Scanner(System.in);
       try{
       System.out.println("Conectando a base de datos... ");
        Class.forName("com.mysql.jdbc.Driver");
        //Connection con = DriverManager.getConnection(url2, user, password);
        Connection con = DriverManager.getConnection(url3, user2, password2);
        System.out.println("Conexion exitosa");
        Statement estado =con.createStatement();
        ResultSet resultado;
       do{        
        datos.Menu();
            opc=valor.next();
            switch(opc){
               case "1": 
                    datos.agregar();
                    //estado.executeUpdate("INSERT INTO `tiendasof`.`inventario` (`id`, `Nombre`, `Cantidad`, `Ventas`, `Precio`, `Ganancias`) VALUES (NULL, '"+datos.nombre+"', '"+datos.cantidad+"', '0', '"+datos.precio+"', '0');");
                    estado.executeUpdate("INSERT INTO `tiendamovil`.`inventario` (`id`, `Nombre`, `Cantidad`, `Ventas`, `Precio`, `Ganancias`) VALUES (NULL, '"+datos.nombre+"', '"+datos.cantidad+"', '0', '"+datos.precio+"', '0');");
                    System.out.println("Producto Inventariado ");
                    break;
                case "2":
                    System.out.println("Ingrese el nombre del producto que quiere buscar:  ");
                    nombre=valor.next();
                    resultado = estado.executeQuery("SELECT * FROM `inventario` WHERE `Nombre` LIKE '"+nombre+"'");
                    while(resultado.next()){// pregunto si hay algo en la siguiente posicion
                        flag3=1;
                        System.out.println("Nombre    Cantidad  Precio");
                        System.out.println(resultado.getString("Nombre") +"\t"+ resultado.getString("Cantidad")+"\t"+ resultado.getString("Ventas")+"\t"+ resultado.getString("Precio")+"\t"+ resultado.getString("Ganancias"));
                    }
                    if(flag3==0)System.out.println("Ese producto no existe");
                    break;
                case "3":
                    System.out.println("Ingrese el nombre del prodcuto a eliminar: ");
                    nombre=valor.next();
                    resultado = estado.executeQuery("SELECT * FROM `inventario` WHERE `Nombre` LIKE '"+nombre+"'");
                    if(resultado.next()){
                        estado.executeUpdate("DELETE FROM `inventario` WHERE `Nombre` LIKE '"+nombre+"'");
                        System.out.println("Producto Eliminado");
                    }else System.out.println("Ese producto no existe");
                    break;
                case "4":
                    System.out.println("Esta son los productos con los que cuenta la TiendaSoft V2.0");
                    System.out.println("               con Base de Datos online                   ");
                    System.out.println("  "); 
                    System.out.println("Nombre    Cantidad  Precio");
                    System.out.println("  ");
                    resultado = estado.executeQuery("SELECT * FROM `inventario`");
                    while(resultado.next()){// pregunto si hay algo en la siguiente posicion
                    System.out.println(resultado.getString("Nombre") +"\t"+ resultado.getString("Cantidad")+"\t"+resultado.getString("Precio"));
                    }
                    break;
                case "5":
                    System.out.println("Cual producto quiere comprar: ");
                    nombre=valor.next();
                    resultado = estado.executeQuery("SELECT * FROM `inventario` WHERE `Nombre` LIKE '"+nombre+"'");
                    if(resultado.next()){
                        //while(resultado.next()){// pregunto si hay algo en la siguiente posicion
                            datos.cantidad=resultado.getInt("Cantidad");
                            datos.ventasTotales=resultado.getInt("Ventas");
                            datos.precio=resultado.getInt("Precio");
                            System.out.println("Cantidad: "+datos.cantidad);
                            System.out.println("Precio: "+datos.precio);                    
                            datos.flag=1;
                        //}
                        if(datos.flag==1){
                            datos.venta();
                            if(datos.flag==2)estado.executeUpdate("UPDATE `inventario` SET `Cantidad`="+datos.cantidad+",`Ventas`="+datos.ventasTotales+",`Ganancias`="+datos.ganancias+" WHERE `Nombre`='"+nombre+"'");
                    }
                    }else {
                        System.out.println(" cuatro ");
                        System.out.println("Ese producto no existe");
                    }
                    datos.flag=0;
                    break;
                case "6":
                    System.out.println("Ganancias Totales");
                    System.out.println("  ");                    
                    System.out.println("Nombre    Cantidad  Ventas Precio  Ganancias");
                    System.out.println("  ");
                    resultado = estado.executeQuery("SELECT * FROM `inventario`");
                    while(resultado.next()){// pregunto si hay algo en la siguiente posicion
                    System.out.println(resultado.getString("Nombre") +"\t\t"+ resultado.getString("Cantidad")+"\t"+ resultado.getString("Ventas")+"\t"+ resultado.getString("Precio")+"\t"+ resultado.getString("Ganancias"));
                    }
                    break;
                case "7":
                    System.out.println("Gracias");
                    break;
                    default:
                    System.out.println("Opcion invalida");
                    break;
            }
             flag3=0;
           }while(!"7".equals(opc));
               }catch (SQLException ex) {
            System.out.println("Error de mysql");
        }catch(Exception e){
            
            System.out.println("Error de tipo " +e.getMessage());
        }
    }  
}