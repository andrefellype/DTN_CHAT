package br.edu.ifnmg.dtnchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import br.edu.ifnmg.dtnchat.dao.ClienteDAO;
import br.edu.ifnmg.dtnchat.dao.DatabaseHelper;
import br.edu.ifnmg.dtnchat.dao.MensagemDAO;
import br.edu.ifnmg.dtnchat.dao.UsuarioDAO;
import br.edu.ifnmg.dtnchat.entidade.Usuario;
import br.edu.ifnmg.dtnchat.fragment.FragmentCliente;
import br.edu.ifnmg.dtnchat.fragment.FragmentDados;
import br.edu.ifnmg.dtnchat.fragment.FragmentHome;
import br.edu.ifnmg.dtnchat.fragment.FragmentMsg;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try{
            DatabaseHelper dh = new DatabaseHelper(this);
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());

            List<Usuario> usuarios = usuarioDAO.queryForAll();
            if(!usuarios.isEmpty()){
                this.usuario = usuarios.get(0);
            } else {
                Intent intent = new Intent(this, BeginActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }
        this.displaySelectedScreen(R.id.action_refresh);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            this.displaySelectedScreen(R.id.action_refresh);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.nav_sair){
            this.sair();
            Intent intent = new Intent(MainActivity.this, BeginActivity.class);
            startActivity(intent);
            finish();
        } else {
            this.displaySelectedScreen(item.getItemId());
        }
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.action_refresh:
                fragment = new FragmentHome(this.usuario);
                break;
            case R.id.nav_mensagens:
                fragment = new FragmentMsg(this.usuario);
                break;
            case R.id.nav_clientes:
                fragment = new FragmentCliente(this.usuario);
                break;
            case R.id.nav_dados:
                fragment = new FragmentDados(this.usuario);
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_main, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void sair(){
        try{
            DatabaseHelper dh = new DatabaseHelper(MainActivity.this);
            MensagemDAO mensagemDAO = new MensagemDAO(dh.getConnectionSource());
            ClienteDAO clienteDAO = new ClienteDAO(dh.getConnectionSource());
            UsuarioDAO usuarioDAO = new UsuarioDAO(dh.getConnectionSource());
            mensagemDAO.delete(mensagemDAO.queryForAll());
            clienteDAO.delete(clienteDAO.queryForAll());
            usuarioDAO.delete(usuarioDAO.queryForAll());
        }catch (Exception e){
            Log.e("FAIL", e.getMessage());
        }
    }
}
