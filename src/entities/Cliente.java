package entities;

import java.util.ArrayList;
import java.util.Arrays;
import helpers.InvalidDataException;
import helpers.MissingValuesException;
import java.io.Serializable;

public class Cliente implements Serializable{
	private String tipoId;
	private long  numeroId;
	private String nombres;
	private String apellidos;
	private String ciudad;
	private Cuenta cuenta;
	
	public Cliente(String tipoId, long numeroId, String nombres, String apellidos, String ciudad ) throws InvalidDataException,MissingValuesException
	{
		if(tipoId.equals("") || numeroId <= 0 || nombres.equals("") || apellidos.equals("") || ciudad.equals("")) throw new MissingValuesException();
		this.setTipoIdentificacion(tipoId);
		this.numeroId = numeroId;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.ciudad = ciudad;
	}
	
	private void setTipoIdentificacion(String tipoId) throws InvalidDataException
	{
		if(!Arrays.asList("cc", "ti", "ce").contains(tipoId.toLowerCase()))
		{
			throw new InvalidDataException();
		}
		this.tipoId = tipoId.toLowerCase();
	}
	
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	public Cuenta getCuenta(){
		return this.cuenta;
	}
	public String getFullName() {
		return this.nombres + " " + this.apellidos;
	}

	public long getNumeroId() {
		return this.numeroId;
	}

}
