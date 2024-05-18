package banco;


import entities.Cliente;
import entities.Cuenta;
import entities.Movimiento;
import helpers.AccountBelowZeroException;
import helpers.AccountNotFoundException;
import helpers.AlreadyExistingException;
import helpers.ClientDoesntExistException;
import helpers.InvalidDataException;
import helpers.MissingValuesException;
import helpers.NotEnoughMoneyException;
import repository.FilesManagement;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Banco {
	
	private Map<Long, Cliente> clientes;
	private Map<Long, Cuenta> cuentas;
	
	private class Tuple{
		public Cuenta cuenta;
		public Cliente cliente;
		
		public Tuple(Cuenta cuenta, Cliente cliente) {
			this.cuenta = cuenta;
			this.cliente = cliente;
		}
	}
	
	public Banco(String clientes, String cuentas) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		this.clientes = FilesManagement.<Cliente>leerFile(clientes);
		this.cuentas = FilesManagement.<Cuenta>leerFile(cuentas);
	}

	public boolean addClient(String tipoId, long id, String nombres, String apellidos, String ciudad) throws InvalidDataException, AlreadyExistingException, MissingValuesException{
		if(this.clientes.get(id) != null) throw new AlreadyExistingException();
		Cliente newClient = new Cliente(tipoId, id, nombres, apellidos, ciudad);
		this.clientes.put(id, newClient);
		return true;	
	}

	public boolean addCuenta(long numeroId, long numeroCuenta, char tipoCuenta, double saldoInicial) throws ClientDoesntExistException, MissingValuesException, AlreadyExistingException, InvalidDataException, NotEnoughMoneyException {
		Cliente client = this.clientes.get(numeroId);
		if(client == null) throw new ClientDoesntExistException();
		if(this.cuentas.get(numeroCuenta) != null ) throw new AlreadyExistingException();
		Cuenta newCuenta = new Cuenta(numeroId, numeroCuenta, tipoCuenta, saldoInicial);
		client.setCuenta(newCuenta);
		this.cuentas.put(numeroCuenta, newCuenta);
		return true;
	}

	public Map<String, String> buscarCuentaExterior(long numero) throws AccountNotFoundException{
		Tuple tuple = this.buscarCuenta(numero);
		Cuenta cuenta = tuple.cuenta; Cliente cliente = tuple.cliente;
		Map<String, String> mapa = new HashMap<>();
		mapa.put("cliente", cliente.getFullName());
		mapa.put("cuenta", Long.toString(cuenta.getNumCuenta()));
		mapa.put("tipo", cuenta.getTipo() + "");
		mapa.put("saldo", Double.toString(cuenta.getSaldoActual()));
		return mapa;	
	}
	
	private Tuple buscarCuenta(long numero) throws AccountNotFoundException{
		Cuenta cuenta = this.cuentas.get(new Long(numero));
		if(cuenta == null) cuenta = this.clientes.get(new Long(numero)).getCuenta();
		if(cuenta == null) throw new AccountNotFoundException();
		Cliente cliente = this.clientes.get(new Long(cuenta.getNumeroId()));
		return new Tuple(cuenta, cliente);
	}

	public Map<String, String> registrarMovimiento(long nCuenta, String tipo, double valor) throws AccountNotFoundException, InvalidDataException, AccountBelowZeroException, MissingValuesException {
		Cuenta cuenta = this.cuentas.get(nCuenta);
		if(cuenta == null) throw new AccountNotFoundException();
		Movimiento mov = new Movimiento(cuenta.getNumeroId(), tipo, valor, cuenta.getSaldoActual());
		mov.setConsecutivo(cuenta.getConsecutivo());
		cuenta.addMovimiento(mov);
		Map<String, String> mapa = new HashMap<>();
		mapa.put("cliente", this.clientes.get(cuenta.getNumeroId()).getFullName());
		mapa.put("cuenta", Long.toString(nCuenta));
		mapa.put("tipo", cuenta.getTipo());
		mapa.put("valor", Double.toString(valor));
		mapa.put("saldo", Double.toString(cuenta.getSaldoActual()));
		return mapa;
	}

	public Map<String, Object> buscarConsecutivos(long numero) throws AccountNotFoundException{
		Tuple tuple  = buscarCuenta(numero);
		Cuenta cuenta = tuple.cuenta; Cliente cliente = tuple.cliente;
		List<Movimiento> movs = cuenta.getMovimientos();
		Map<String, Object> mapa = new HashMap<>();
		mapa.put("cliente", cliente.getFullName());
		mapa.put("cuenta", cuenta.getNumCuenta());
		mapa.put("movimientos", movs);
		return mapa;
	}

	public void guardarData() throws IOException {
		FilesManagement.<Cliente>writeFile(new LinkedList<Cliente>(this.clientes.values()), "clientes");
		FilesManagement.<Cuenta>writeFile(new LinkedList<Cuenta>(this.cuentas.values()), "cuentas");
	}
	
}
