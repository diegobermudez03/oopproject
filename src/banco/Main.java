package banco;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.ECPoint;
import java.util.HashMap;
import java.util.Scanner;

import entities.Movimiento;
import helpers.AccountNotFoundException;
import helpers.MyFunction;

public class Main {
	
	private static Map<String, String> excepciones;
	
	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException
	{
		poblarMapa();
		Scanner lector = new Scanner(System.in);
		byte op = 0;
		Banco banco = new Banco("clientes", "cuentas");
		do
		{
			System.out.println("\n\nBanco XYZ \n\n1. Registro de clientes \n2. Creacion de cuentas \n3. Consulta de cuentas");
			System.out.println("4. Registro de movimientos \n5. Consulta de movimientos \n6. Reporte general por cliente \n7. Salir \n\nOpcion: ");
			try {
				op = lector.nextByte();
				lector.nextLine();
				switch(op)
				{
				case 1:registrarCliente(lector, banco);break;
				case 2:crearCuenta(lector, banco);break;
				case 3:buscarCuenta(lector, banco);break;
				case 4:realizarMovimiento(lector, banco);break;
				case 5:consultaMovimiento(lector, banco);break;
				case 6:reporteGeneral(lector, banco); break;
				case 7:System.out.println("\nAdioss");break;
				default:System.out.println("\nIngrese una opcion valida"); break;
				}
				
			}catch(InputMismatchException e){
				System.out.println("La opcion debe ser numerica");	
				lector.nextLine();
			}
		}while(op != 7);
		guardarData(banco);
	}

	private static void guardarData(Banco myBanco) {
		Main.errorWrapper((__, banco)-> {
			banco.guardarData();
			System.out.println("\u001B[32m" + "Informacion guardada, buen dia" +"\u001B[0m");
		}, null, myBanco);	
	}

	private static void reporteGeneral(Scanner scannerlector, Banco myBanco) {
		Main.errorWrapper((lector, banco)->{
			System.out.println("\nIngrese el numero de cuenta o cliente: ");
			long numero = lector.nextLong();
			System.out.println("--INFORME GENERAL--");
			handleCliente(numero, banco);
			System.out.println("--INFORME MOVIMIENTOS--");
			handleMovimientos(numero,banco, true);
		}, scannerlector, myBanco);
	}
	
	private static void consultaMovimiento(Scanner scannerlector, Banco myBanco) {
		Main.errorWrapper((lector, banco)->{
			System.out.println("\nIngrese el numero de cuenta o cliente: ");
			long numero = lector.nextLong();
			handleMovimientos(numero, banco, false);
		},scannerlector, myBanco);
	}
	
	private static void handleMovimientos(long numero, Banco banco, boolean reporte) throws AccountNotFoundException {
		Map<String, Object> response = banco.buscarConsecutivos(numero);
		System.out.println("\nConsecutivo" + (reporte? "" : "---Cliente---cuenta") +"---Tipo Movimiento---Valor\n");
		for(Movimiento mov: ((LinkedList<Movimiento>)response.get("movimientos"))) {
			System.out.println(mov.getConsecutivo() + "           " + (reporte ? "":(response.get("cliente") + "    " + response.get("cuenta"))));
			System.out.print("   " + mov.getTipo() + "   $" + mov.getValor());
		}
	}

	private static void realizarMovimiento(Scanner scannerlector, Banco myBanco) {
		Main.errorWrapper((lector, banco)->{
			System.out.println("\nIngrese el numero de cuenta");
			long cuenta = lector.nextLong();
			lector.nextLine();
			System.out.println("\nQue tipo de movimiento desea realizar? (R: retiro, C: consignacion):");
			String tipo = lector.nextLine();
			System.out.println("\nIngrese el valor del movimiento: ");
			double valor = lector.nextDouble();
			Map<String, String> response = banco.registrarMovimiento(cuenta, tipo, valor);
			System.out.println("\nCliente: " + response.get("cliente") + "\nCuenta: " + response.get("cuenta"));
			System.out.println("Tipo movimiento: " + response.get("tipo") + "\nValor: " + response.get("valor"));
			System.out.println("Saldo actual: " + response.get("saldo"));
		}, scannerlector, myBanco);
	}

	private static void buscarCuenta(Scanner scannerlector, Banco myBanco) {
		Main.errorWrapper((lector, banco) -> {
			System.out.println("\nIngrese el numero de cuenta o cliente: ");
			long numero = lector.nextLong();
			handleCliente(numero, banco);
		}, scannerlector, myBanco);
	}
	
	private static void handleCliente(long numero, Banco banco) throws AccountNotFoundException {
		Map<String, String> response = banco.buscarCuentaExterior(numero);
		System.out.println("=======================");
		System.out.println("\nCliente: " + response.get("cliente") + "\nCuenta: " + response.get("cuenta"));
		System.out.println("Tipo: " + response.get("tipo")+ "\nSaldo actual " + response.get("saldo"));
	}


	private static void crearCuenta(Scanner scannerlector, Banco myBanco) {
		Main.errorWrapper((lector, banco) -> {
			System.out.println("\n\nIngrese el numero de identificacion del cliente: ");
			long numeroId = lector.nextLong();
			System.out.println("\n\nIngrese el numero de cuenta: ");
			long numeroCuenta = lector.nextLong();
			lector.nextLine();
			System.out.println("\n\nIngrese el tipo de cuenta ( A: ahorros, C: corriente) : ");
			char tipoCuenta = lector.nextLine().charAt(0);
			System.out.println("\n\nIngrese el saldo de apertura (> 50k): ");
			double saldoInicial = lector.nextDouble();
			if(banco.addCuenta(numeroId, numeroCuenta, tipoCuenta, saldoInicial)) 
					System.out.println("\u001B[32m"+ "Cuenta creada exitosamente" + "\u001B[0m");
			else System.out.println("\u001B[31m" +"No se pudo crear la cuenta" + "\u001B[0m");
		}, scannerlector, myBanco);
	}

	public static void registrarCliente(Scanner scannerlector, Banco myBanco){
		
		Main.errorWrapper((lector, banco) -> {
			System.out.println("\n\nIngrese el tipo de identificacion (CC, TI, CE): ");
			String tipoId = lector.nextLine();
			System.out.println("\nIngrese el numero de identificacion del cliente: ");
			long id = lector.nextLong();
			lector.nextLine();	//para consumir el buffer
			System.out.println("\nIngrese los nombres del cliente: ");
			String nombres = lector.nextLine();
			System.out.println("\nIngrese los apellidos del cliente: ");
			String apellidos = lector.nextLine();
			System.out.println("\nIngrese la ciudad de la oficina : ");
			String ciudad = lector.nextLine();
			if(banco.addClient(tipoId, id, nombres, apellidos, ciudad)) {
				System.out.println("\u001B[32m" +"\nCliente registrado exitosamente" +"\u001B[0m");
				return;
			}
			System.out.println("\u001B[31m"+ "No se pudó registrar el cliente" +"\u001B[0m");
		}, scannerlector, myBanco);
	}
	
	
	private static void poblarMapa() {
		Main.excepciones = new HashMap<String, String>();
		Main.excepciones.put("helpers.AlreadyExistingException", "\nEse numero ya esta siendo usado");
		Main.excepciones.put("helpers.ClientDoesntExistException", "\nEl cliente asociado a la cuenta no ha sido registrado");
		Main.excepciones.put("helpers.InvalidDataException", "\nLos datos no cumplen con el esquema solicitado");
		Main.excepciones.put("helpers.MissingValuesException", "\nDebe ingresar todos los datos");
		Main.excepciones.put("helpers.NotEnoughMoneyException", "\nEl saldo de apertura debe ser mayor a 50k");
		Main.excepciones.put("helpers.InputMismatchException", "\nAsegurese de respetar los campos de tipo numerico");
		Main.excepciones.put("helpers.AccountNotFoundException", "\nNo se encontro la cuenta");
		Main.excepciones.put("helpers.AccountBelowZeroException", "\nEl retiro deja la cuenta en negativo");
	}
	
	private static void errorWrapper(MyFunction<Scanner, Banco, Exception> funcion, Scanner lector, Banco banco) {
		try {
			funcion.apply(lector, banco);
		}
		catch(Exception e) {
			Class<?> exceptionClass = e.getClass();
			String exceptionName = exceptionClass.getName();
			if(Main.excepciones.get(exceptionName) != null) {
				System.out.println("\u001B[31m" +Main.excepciones.get(exceptionName) + "\u001B[0m");
				if(exceptionName.equals("helpers.InputMismatchException")) lector.nextLine();
			}else {
				lector.nextLine();
				System.out.println("\u001B[31m" + "Sucedio un error, verifique ingresar los tipos correctos" + "\u001B[0m");
			}
		}
	}

}
