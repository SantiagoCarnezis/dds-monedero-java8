package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cuenta {

  private BigDecimal saldo;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta(BigDecimal montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void depositar(BigDecimal cuanto) {
    if (cuanto.compareTo(new BigDecimal(0)) <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (cantidadDeDepositos() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los 3 depositos diarios");
    }

    agregarMovimiento(LocalDate.now(), cuanto, new Deposito());
  }


  public void extraer(BigDecimal cuanto) {
    if (cuanto.compareTo(new BigDecimal(0)) <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
    if (saldoInsuficiente(cuanto)) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }

    BigDecimal limite = new BigDecimal(1000).subtract(getMontoExtraidoA(LocalDate.now()));
    if (cuanto.compareTo(limite) > 0) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de 1000$ diarios, límite: " + limite);
    }
    agregarMovimiento(LocalDate.now(), cuanto, new Extraccion());
  }

  boolean saldoInsuficiente(BigDecimal cuanto){
    return getSaldo().subtract(cuanto).compareTo(new BigDecimal(0)) < 0;
  }

  public void agregarMovimiento(LocalDate fecha, BigDecimal cuanto, TipoMovimiento tipo) {
    Movimiento movimiento = new Movimiento(fecha, cuanto, tipo);
    movimientos.add(movimiento);
    saldo = tipo.calcularSaldo(this.saldo, cuanto);
  }

  public BigDecimal getMontoExtraidoA(LocalDate fecha) {
    List<BigDecimal> listaMontos = getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .map(Movimiento::getMonto).collect(Collectors.toList());
    BigDecimal sumaTotal = new BigDecimal(0);
    for(int c =0; c < listaMontos.size(); c++){
      BigDecimal monto = listaMontos.get(c);
      sumaTotal = sumaTotal.add(monto);
    }
    return sumaTotal;
  }


  public long cantidadDeDepositos(){
    return getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count();
  }

  public long cantidadDeExtracciones(){
    return getMovimientos().stream().filter(movimiento -> !movimiento.isDeposito()).count();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

}
