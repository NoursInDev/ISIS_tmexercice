package ticketmachine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TicketMachineSpecificationTest {
	private static final int PRICE = 50;

	private TicketMachine machine;

	@BeforeEach
	void setUp() {
		machine = new TicketMachine(PRICE);
	}

	// S1 : le prix affiché correspond à l’initialisation
	@Test
	void s1_priceIsCorrectlyInitialized() {
		assertEquals(PRICE, machine.getPrice(), "Initialisation incorrecte du prix");
	}

	// S2 : la balance change quand on insère de l’argent
	@Test
	void s2_insertMoneyChangesBalance() {
		machine.insertMoney(10);
		machine.insertMoney(20);
		assertEquals(10 + 20, machine.getBalance(), "La balance n'est pas correctement mise à jour");
	}

	// S3 : on n’imprime pas le ticket si le montant inséré est insuffisant
	@Test
	void s3_doNotPrintWhenInsufficientFunds() {
		machine.insertMoney(PRICE - 10); // less than price
		boolean printed = machine.printTicket();
		assertFalse(printed, "Le ticket ne devrait pas être imprimé si les fonds sont insuffisants");
		// total and balance should remain unchanged
		assertEquals(PRICE - 10, machine.getBalance(), "La balance ne doit pas changer si l'impression échoue");
		assertEquals(0, machine.getTotal(), "Le total ne doit pas être mis à jour si aucun ticket n'a été imprimé");
	}

	// S4 : on imprime le ticket si le montant inséré est suffisant
	@Test
	void s4_printWhenSufficientFunds() {
		machine.insertMoney(PRICE);
		boolean printed = machine.printTicket();
		assertTrue(printed, "Le ticket devrait être imprimé si les fonds sont suffisants");
	}

	// S5 : Quand on imprime un ticket la balance est décrémentée du prix du ticket
	@Test
	void s5_balanceDecrementedWhenPrinting() {
		machine.insertMoney(PRICE + 20);
		boolean printed = machine.printTicket();
		assertTrue(printed, "Expected ticket to be printed");
		assertEquals(20, machine.getBalance(), "La balance doit être décrémentée du prix du ticket après impression");
	}

	// S6 : le montant collecté est mis à jour quand on imprime un ticket (pas avant)
	@Test
	void s6_totalUpdatedOnlyOnPrint() {
		machine.insertMoney(PRICE);
		// before printing total must be unchanged
		assertEquals(0, machine.getTotal(), "Le total ne doit pas être mis à jour avant l'impression");
		boolean printed = machine.printTicket();
		assertTrue(printed, "Expected ticket to be printed");
		assertEquals(PRICE, machine.getTotal(), "Le total doit être mis à jour après impression");
	}

	// S7 : refund() rend correctement la monnaie
	@Test
	void s7_refundReturnsCurrentBalance() {
		machine.insertMoney(30);
		int refunded = machine.refund();
		assertEquals(30, refunded, "refund() doit retourner la somme insérée");
	}

	// S8 : refund() remet la balance à zéro
	@Test
	void s8_refundResetsBalance() {
		machine.insertMoney(30);
		machine.refund();
		assertEquals(0, machine.getBalance(), "refund() doit remettre la balance à zéro");
	}

	// S9 : on ne peut pas insérer un montant négatif
	@Test
	void s9_cannotInsertNegativeAmount() {
		assertThrows(IllegalArgumentException.class, () -> machine.insertMoney(-10),
				"Insérer un montant négatif doit lancer IllegalArgumentException");
	}

	// S10 : on ne peut pas créer de machine qui délivre des tickets dont le prix est négatif
	@Test
	void s10_cannotCreateMachineWithNegativePrice() {
		assertThrows(IllegalArgumentException.class, () -> new TicketMachine(-50),
				"Créer une machine avec un prix négatif doit lancer IllegalArgumentException");
	}
}
