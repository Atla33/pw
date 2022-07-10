package com.example.computadores.controller;

import com.example.computadores.domain.Computador;
import com.example.computadores.service.ComputadorService;
import com.example.computadores.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ComputadorController {

    private final ComputadorService service;
    private final FileStorageService fileStorageService;

    public ComputadorController(ComputadorService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String getComputadorHome(Model model, HttpServletResponse response){

        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        Cookie cookie = new Cookie("visita","cookie-value");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return "index";
    }

    @GetMapping("/cadastrar")
    public String doCadastrar(Model model){
        Computador c = new Computador();
        model.addAttribute("computador", c);

        return "cadastrar";

    }

    @GetMapping("editar/{id}")
    public String getEditarComputador(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes){

        Computador computador = service.findById(id);
        model.addAttribute("computador", computador);

        redirectAttributes.addAttribute("msg2", "Cadastro atualizado com sucesso");
        return "cadastrar";
    }

    @GetMapping("deletar/{id}")
    public String getDeletarComputador(@ModelAttribute Computador c, Model model, @PathVariable Long id){
        System.out.println(c.getDescricao());
        Computador computador = service.findById(c.getId());
        computador.setDescricao(c.getDescricao());
        computador.setImagem(c.getImagem());
        computador.setMarca(c.getMarca());
        computador.setModelo(c.getModelo());
        computador.setPreco(c.getPreco());
        computador.setDeletd(false);
        service.update(computador);
        List<Computador> computadores = service.findAll();
        model.addAttribute("computador", computadores);
        return "index";
    }

    @PostMapping("salvar")
    public String doSalvaComputador(@ModelAttribute @Valid Computador c, Errors errors,
                                    @RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request){

        if (errors.hasErrors()){
            System.out.println(errors.getAllErrors().stream().toArray());
            return "produto/cadastrar";
        }else{
            /*
			System.out.println(file.getOriginalFilename());
			System.out.println(file.getContentType());
			System.out.println(file.getSize());
             */

            c.setImagem(file.getOriginalFilename());
            service.update(c);
            fileStorageService.save(file);

            redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");
            return "redirect:/";
        }
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String getAddCarrinho(Model model, @PathVariable Long id){

        Computador computador = service.findById(id);
        model.addAttribute("computador", computador);

        return "cadastrar";
    }

    @GetMapping("/vercarrinho")
    public String getVerCarrinho(Model model){
        return "vercarrinho";
    }
}
