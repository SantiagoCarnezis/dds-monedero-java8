package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Depositar1500EnUnaCuenta() {
    cuenta.poner(1500);

    assertEquals(1, cuenta.cantidadDeDepositos());
    assertEquals(1500, cuenta.getSaldo());
  }

  @Test
  void NoSePuedeDepositarUnMontonNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
  }

  @Test
  void NoSePuedenRealizarMasDeTresDepositosEnUnDia() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
    assertEquals(3856, cuenta.getSaldo());
  }

  /*
   * Es innecesario hacer un test que haga 3 poner y otro que haga mas de
   * 3 poner ya que ambos casos pertenecen a la misma clase de equivalencia
   */
  @Test
  void Extraer1000DeUnaCuenta() {
    cuenta.poner(1500);
    cuenta.sacar(1000);

    assertEquals(1, cuenta.cantidadDeExtracciones());
    assertEquals(500, cuenta.getSaldo());
  }

  @Test
  void NoSePuedeExtraerUnMontoMayorAlSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(100);
    });
  }

  @Test
  public void ElMontoMaximoAExtraerEnUnDiaEs1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(100);
      cuenta.sacar(901);
    });
    assertEquals(4900, cuenta.getSaldo());
  }

  @Test
  public void NoSePuedeExtraerUnMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

}