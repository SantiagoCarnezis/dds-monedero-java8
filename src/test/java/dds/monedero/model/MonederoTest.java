package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta(new BigDecimal(0));
  }

  @Test
  void Depositar1500EnUnaCuenta() {
    cuenta.depositar(new BigDecimal(1500));

    assertEquals(1, cuenta.cantidadDeDepositos());
    //assertTrue(new BigDecimal(1500).compareTo(cuenta.getSaldo()) == 0);
    assertEquals(new BigDecimal(1500),cuenta.getSaldo());
  }

  @Test
  void NoSePuedeDepositarUnMontonNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.depositar(new BigDecimal(-1500)));
  }

  @Test
  void NoSePuedenRealizarMasDeTresDepositosEnUnDia() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.depositar(new BigDecimal(1500));
          cuenta.depositar(new BigDecimal(456));
          cuenta.depositar(new BigDecimal(1900));
          cuenta.depositar(new BigDecimal(245));
    });
    assertEquals(new BigDecimal(3856), cuenta.getSaldo());
  }

  @Test
  void Extraer1000DeUnaCuenta() {
    cuenta.setSaldo(new BigDecimal(1500));
    cuenta.extraer(new BigDecimal(1000));

    assertEquals(1, cuenta.cantidadDeExtracciones());
    assertEquals(new BigDecimal(500), cuenta.getSaldo());
  }

  @Test
  void NoSePuedeExtraerUnMontoMayorAlSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(new BigDecimal(90));
          cuenta.extraer(new BigDecimal(100));
    });
  }

  @Test
  public void ElMontoMaximoAExtraerEnUnDiaEs1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(new BigDecimal(5000));
      cuenta.extraer(new BigDecimal(100));
      cuenta.extraer(new BigDecimal(901));
    });
    assertEquals(new BigDecimal(4900),cuenta.getSaldo());
  }

  @Test
  public void NoSePuedeExtraerUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.extraer(new BigDecimal(-500)));
  }

}