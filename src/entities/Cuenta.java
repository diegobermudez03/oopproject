package entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;


import helpers.InvalidDataException;
import helpers.MissingValuesException;
import helpers.NotEnoughMoneyException;

public class Cuenta implements Serializable{
	private long numeroId;
	private long numCuenta;
	private char tipo;
	private double saldoApertura;
	private double saldoActual;
	private List<Movimiento> movimientos;

	public Cuenta(long numeroId, long numeroCuenta, char tipoCuenta, double saldoInicial)throws  MissingValuesException, InvalidDataException, NotEnoughMoneyException{
		if(numeroId <= 0 || numeroCuenta <= 0 || tipoCuenta == ' ' || saldoInicial <= 0 ) throw new MissingValuesException();
		this.setTipo(tipoCuenta);
		this.setSaldoApertura(saldoInicial);
		this.numeroId = numeroId;
		this.numCuenta = numeroCuenta;
		this.movimientos = new LinkedList<>();
	}

	private void setTipo(char tipoCuenta) throws InvalidDataException{
		String tipo = tipoCuenta + "";
		if(!Arrays.asList("a", "c").contains(tipo.toLowerCase())) throw new InvalidDataException();
		this.tipo = tipo.toLowerCase().charAt(0);
	}
	
	public int getConsecutivo() {
		//opcion alternativa simlemente hacr 
		//return this.movimientos.lenght(), pero por mayor precision se hace el for 
		int mayor = -1;
		for(Movimiento mov : movimientos) {
			if(mov.getConsecutivo() > mayor) mayor = mov.getConsecutivo();
		}
		return mayor + 1;
	}
	
	private void setSaldoApertura(double saldo)throws NotEnoughMoneyException {
		if(saldo <= 50000) throw new NotEnoughMoneyException();
		this.saldoApertura = saldo;
		this.saldoActual = saldo;
	}
	
	public long getNumeroId(){
		return this.numeroId;
	}

	public long getNumCuenta() {
		return this.numCuenta;
	}
	
	public String getTipo() {
		return this.tipo == 'a' ? "Ahorros" : "Corriente";
	}
	
	public double getSaldoActual() {
		return this.saldoActual;
	}

	public void addMovimiento(Movimiento mov) {
		this.saldoActual += mov.getOperacion();
		this.movimientos.add(mov);	
	}

	public List<Movimiento> getMovimientos() {
		return this.movimientos;
	}

}
