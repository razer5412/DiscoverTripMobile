package tn.rnu.isetr.projetntegrationmobile

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import tn.rnu.isetr.projetntegrationmobile.util.Validators

class ValidatorsTest {

    @Test
    fun email_valide_est_accepte() {
        assertTrue(Validators.emailValide("tasnim@test.tn"))
    }

    @Test
    fun email_sans_arobase_est_refuse() {
        assertFalse(Validators.emailValide("tasnim.test.tn"))
        assertNotNull(Validators.erreurEmail("abc"))
    }

    @Test
    fun mot_de_passe_valide_est_accepte() {
        assertTrue(Validators.motDePasseValide("Test1234"))
    }

    @Test
    fun mot_de_passe_trop_court_est_refuse() {
        assertFalse(Validators.motDePasseValide("Ab1"))
    }

    @Test
    fun mot_de_passe_sans_chiffre_est_refuse() {
        assertFalse(Validators.motDePasseValide("abcdefgh"))
    }

    @Test
    fun mot_de_passe_sans_lettre_est_refuse() {
        assertFalse(Validators.motDePasseValide("12345678"))
    }

    @Test
    fun confirmation_differente_est_refusee() {
        assertFalse(Validators.confirmationValide("Test1234", "Test12345"))
        assertNotNull(Validators.erreurConfirmation("Test1234", "autre"))
    }

    @Test
    fun confirmation_identique_est_acceptee() {
        assertNull(Validators.erreurConfirmation("Test1234", "Test1234"))
    }

    @Test
    fun telephone_vide_est_accepte_car_facultatif() {
        assertTrue(Validators.telephoneValide(""))
    }

    @Test
    fun telephone_de_8_chiffres_est_accepte() {
        assertTrue(Validators.telephoneValide("20 123 456"))
    }

    @Test
    fun telephone_trop_court_ou_avec_lettres_est_refuse() {
        assertFalse(Validators.telephoneValide("1234567"))
        assertFalse(Validators.telephoneValide("20abc456"))
    }
}