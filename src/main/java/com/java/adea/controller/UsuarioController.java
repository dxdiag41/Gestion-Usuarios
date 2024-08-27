package com.java.adea.controller;

import com.java.adea.entity.Usuario;
import com.java.adea.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/gestion")
    public String showGestion(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("usuarios", usuarioService.findAllUser());
        return "gestion";
    }

    @GetMapping("/tablero")
    public String tablero(Model model) {
        return "tablero";
    }

    @PostMapping("/guardar")
    public String saveUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.create(usuario);
        return "redirect:/usuarios/gestion";
    }

    @PostMapping("/eliminar")
    public String deleteUsuario(@RequestParam String login) {
        Usuario usuario = usuarioService.findByLogin(login);
        if (usuario != null) {
            usuarioService.delete(usuario);
        }
        return "redirect:/usuarios/gestion";
    }

    @PostMapping("/actualizar")
    public String updateUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.update(usuario);
        return "redirect:/usuarios/gestion";
    }

    @GetMapping("/buscarPorNombre")
    public String buscarPorNombre(@RequestParam String nombre, Model model) {
        List<Usuario> usuarios = usuarioService.findNombre(nombre);
        model.addAttribute("usuarios", usuarios);
        return "tablero";
    }

    @GetMapping("/buscarPorStatus")
    public String buscarPorStatus(@RequestParam Character status, Model model) {
        List<Usuario> usuarios = usuarioService.findStatus(status);
        model.addAttribute("usuarios", usuarios);
        return "tablero";
    }

    @GetMapping("/buscarPorFechaAlta")
    public String buscarPorFechaAlta(@RequestParam java.sql.Date fechaAlta, Model model) {
        List<Usuario> usuarios = usuarioService.findFechaAlta(fechaAlta);
        model.addAttribute("usuarios", usuarios);
        return "tablero";
    }

    @GetMapping("/buscarPorFechaBaja")
    public String buscarPorFechaBaja(@RequestParam java.sql.Date fechaBaja, Model model) {
        List<Usuario> usuarios = usuarioService.findFechaBaja(fechaBaja);
        model.addAttribute("usuarios", usuarios);
        return "tablero";
    }

    @GetMapping("/verificarUsuario")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarUsuario(@RequestParam String login) {
        Usuario usuario = usuarioService.findByLogin(login);
        Map<String, Object> response = new HashMap<>();
        if (usuario != null) {
            response.put("existe", true);
            response.put("login", usuario.getLogin());
            response.put("password", usuario.getPassword());
            response.put("nombre", usuario.getNombre());
            response.put("cliente", usuario.getCliente());
            response.put("email", usuario.getEmail());
            response.put("fechaAlta", usuario.getFechaAlta());
            response.put("fechaBaja", usuario.getFechaBaja());
            response.put("status", usuario.getStatus());
            response.put("intentos", usuario.getIntentos());
            response.put("fechaRevocado", usuario.getFechaRevocado());
            response.put("fechaVigencia", usuario.getFechaVigencia());
            response.put("noAcceso", usuario.getNoAcceso());
            response.put("apellidoPaterno", usuario.getApellidoPaterno());
            response.put("apellidoMaterno", usuario.getApellidoMaterno());
            response.put("area", usuario.getArea());
        } else {
            response.put("existe", false);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/editar")
    public String showEditForm(@RequestParam String login, Model model) {
        Usuario usuario = usuarioService.findByLogin(login);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarios", usuarioService.findAllUser());
            return "gestion";
        } else {
            model.addAttribute("error", "Usuario no encontrado");
            return "redirect:/usuarios/gestion";
        }
    }
}