package entities;

import java.io.Serializable;

import helpers.AccountBelowZeroException;
import helpers.AccountNotFoundException;
import helpers.InvalidDataException;
import helpers.MissingValuesException;

public class Movimiento implements Serializable{
	private int consecutivo;
	private long numeroId;
	private char tipo;
	private double valor;
	
	//no tiene mucho sentido recibir el saldo actual de la cuenta para verificar lo de que 
	//la cuenta quede debajo de 0, pero como en el enunciado del proyecto dice que 
	//la verificacion debe realizarla el metodo setValor DEL MOVIMIENTO, y tambien dice 
	//que al constructor se le pasa e valor, entonces esta es la unica forma de cumplir con ambos
	//requisitos
	public Movimiento(long numeroId,String tipo,double valor, double saldoActual) throws MissingValuesException, InvalidDataException, AccountBelowZeroException
	{
		if(numeroId <= 0 || tipo.equals("") || valor <= 0) throw new MissingValuesException();
		this.setTipoMovimiento(tipo);
		this.setValor(valor);
		if(this.tipo == 'r' && (saldoActual - this.valor < 0)) throw new AccountBelowZeroException();
		this.numeroId = numeroId;
	}
	
	public void setTipoMovimiento(String tipo) throws InvalidDataException
	{
		if( tipo.toLowerCase().equals("r") || tipo.toLowerCase().contains("retiro")) this.tipo = 'r';
		else if(tipo.toLowerCase().equals("c") || tipo.toLowerCase().contains("consignacion")) this.tipo = 'c';
		else throw new InvalidDataException();
	}
	
	private void setValor(double valor) throws InvalidDataException{
		if(valor > 0) this.valor = valor;
		else throw new InvalidDataException();
	}
	
	public int getConsecutivo() {
		return this.consecutivo;
	}
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	public double getOperacion() {
		return this.tipo== 'r' ? -this.valor : this.valor;
	}
	
	public String getTipo() {
		return this.tipo == 'r' ? "RETIRO" : "CONSIGNACION";
	}
	
	public double getValor() {
		return this.valor;
	}
}
