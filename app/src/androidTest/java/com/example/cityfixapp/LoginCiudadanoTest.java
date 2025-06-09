package com.example.cityfixapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.cityfixapp.Activity.Login;
import com.example.cityfixapp.R;

@RunWith(AndroidJUnit4.class)
public class LoginCiudadanoTest {

    @Test
    public void loginCiudadanoCorrecto() {
        ActivityScenario.launch(Login.class);

        onView(withId(R.id.Usuario)).perform(typeText("ciudadano1"), closeSoftKeyboard());
        onView(withId(R.id.Contraseña)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.BTAceptarlogin)).perform(click());

        // Verifica que se muestra el RecyclerView tras login exitoso
        onView(withId(R.id.rvMisIncidencias)).check(matches(isDisplayed()));
    }
}
