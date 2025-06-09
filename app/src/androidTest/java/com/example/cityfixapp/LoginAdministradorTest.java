package com.example.cityfixapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.cityfixapp.Activity.LoginAdministrador;
import com.example.cityfixapp.R;

@RunWith(AndroidJUnit4.class)
public class LoginAdministradorTest {

    @Test
    public void loginAdministradorCorrecto() {
        ActivityScenario.launch(LoginAdministrador.class);

        onView(withId(R.id.Usuario)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.Contraseña)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.BTAceptarlogin)).perform(click());

        // Verificamos que se muestra el RecyclerView de funciones del administrador
        onView(withId(R.id.rvFunciones)).check(matches(isDisplayed()));
    }
}
