package br.com.daviJAVA.Cript.password.controller;

import br.com.daviJAVA.Cript.password.model.UsuarioModel;
import br.com.daviJAVA.Cript.password.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;
    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder){
        this.repository = repository;
        this.encoder = encoder;
    }
    @GetMapping("/listarTodos")
    public ResponseEntity <List<UsuarioModel>> listarTodos(){return ResponseEntity.ok(repository.findAll());}


    @PostMapping("/salvar")
public ResponseEntity<UsuarioModel> salvar(@RequestBody UsuarioModel usuario){
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return ResponseEntity.ok(repository.save(usuario));
}
@GetMapping("/validarSenha")
public ResponseEntity<Boolean> validarSenha(@RequestParam String login,
                                            @RequestParam String password){
    Optional<UsuarioModel> optUsuario = repository.findByLogin(login);
    if (optUsuario.isEmpty()){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }


    boolean valid = encoder.matches(password, optUsuario.get().getPassword());
    HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(valid);
}
}
