package com.example.computadores.controller;

import com.example.computadores.domain.Computador;
import com.example.computadores.service.ComputadorService;
import com.example.computadores.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ComputadorController {

    private final ComputadorService service;
    private final FileStorageService fileStorageService;

    public ComputadorController(ComputadorService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/")
    public String getHome(){
        return "index";
    }

    @GetMapping("/cadastrar")
    public String doCadastrar(Model model){
        Computador c = new Computador();
        model.addAttribute("computador", c);

        return "cadastrar";

    }

    @PostMapping("salvar")
    public String doSalvaComputador(@ModelAttribute @Valid Computador c, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request){

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
}
