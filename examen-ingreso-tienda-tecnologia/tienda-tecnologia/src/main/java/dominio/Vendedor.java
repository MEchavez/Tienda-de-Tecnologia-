package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {
	
    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String EL_PRODUCTO_TIENE_MAL_CODIGO = "El producto tiene más de tres vocales";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.setRepositorioProducto(repositorioProducto);
        this.setRepositorioGarantia(repositorioGarantia);

    }

    private void setRepositorioGarantia(RepositorioGarantiaExtendida repositorioGarantia) {
		// TODO Auto-generated method stub
		this.repositorioGarantia = repositorioGarantia;
	}

	private void setRepositorioProducto(RepositorioProducto repositorioProducto) {
		// TODO Auto-generated method stub
		this.repositorioProducto = repositorioProducto;
	}

	public void generarGarantia(String codigo, String nombreCliente) throws GarantiaExtendidaException {		
    	// No debe estar en RepositorioGarantiaExtendida.		
		if (this.tieneGarantia(codigo)) 
			throw new GarantiaExtendidaException(Vendedor.EL_PRODUCTO_TIENE_GARANTIA);
		
		Producto producto = this.repositorioProducto.obtenerPorCodigo(codigo);
		// Comprobar vocales de garantiaExtendida con tres vocales.
		if (this.contarVocales(codigo) > 3) 
			throw new GarantiaExtendidaException(Vendedor.EL_PRODUCTO_TIENE_MAL_CODIGO);
		
		double precioGarantia = this.calcularCostoGarantia(producto.getPrecio());
		Date fechaInicial = new Date();
		
		Date fechaFinal = this.calcularFechaFinal(fechaInicial, producto.getPrecio());
		
		 // Crear garantia extendida con nombreCliente y fechas.
        GarantiaExtendida nuevaGarantia = new GarantiaExtendida(
        		producto, 
        		fechaInicial, 
        		fechaFinal, 
        		precioGarantia, 
        		nombreCliente);
        
        this.repositorioGarantia.agregar(nuevaGarantia);
    }
	
	private int contarVocales(String codigo) {
		int numVocales = 0;
		for(int i = 0; i < codigo.length(); i++) {
			char actual = codigo.charAt(i);
			  if ((Character.toLowerCase(actual)=='a') || (Character.toLowerCase(actual)=='e') || 
					  (Character.toLowerCase(actual)=='i') || (Character.toLowerCase(actual)=='o') || 
					  (Character.toLowerCase(actual)=='u')) {
				  numVocales++;
			  }
		}
		
		return numVocales; 
	}
	
	private double calcularCostoGarantia(double precioOriginal) {
		final int UMBRAL = 500000;
		double costoFinal = 0;
		
		
		// Si precio mayor a 500000, costo 20% del oroginal por 200 días.
		if (precioOriginal > UMBRAL) {
			costoFinal = precioOriginal * 0.2F;			
		}
		// Sino precio menor a 500000, costo 10% del original por 100 días.
		else {
			costoFinal = precioOriginal * 0.1F;			
		}		

		// contar días 
		return 0;
	}
	
	private Date calcularFechaFinal(Date fechaInicial, double precioOriginal) {
		final int UMBRAL = 500000;
		Calendar auxiliarFecha = Calendar.getInstance();
		auxiliarFecha.setTime(fechaInicial);
		
		if (precioOriginal > UMBRAL)		
			auxiliarFecha.add(Calendar.DATE, 200);
		else 
			auxiliarFecha.add(Calendar.DATE, 100);
		
		if (auxiliarFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			auxiliarFecha.add(Calendar.DATE, 1);
		

		return auxiliarFecha.getTime();
	}

    public boolean tieneGarantia(String codigo) {
    	Producto producto = this.repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
    	
        return (producto != null) ? true : false;
    }
    
    
}
