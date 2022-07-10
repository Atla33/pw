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

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Controller
public class ComputadorController {

    private static int contador = 0;
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

    @GetMapping("/admin")
    public String getComputadoradmin(Model model, HttpServletResponse response){

        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        return "admin";
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
            redirectAttributes.addAttribute("msg", "Cadastro fracassado");
            return "redirect:/admin";
        }else{
            try{ComputadorController.contador++;
                c.setImagem(file.getOriginalFilename() + ComputadorController.contador);
                service.update(c);
                fileStorageService.save(file);

                redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");
                return "redirect:/admin";
            }catch(Exception e){redirectAttributes.addAttribute("msg", "Cadastro fracassado");
                return "redirect:/admin";}
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

    @GetMapping("/addItemCarrinho")
    public void doAdicionarItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var idComputador = 2;
        var computador = service.findById((long) idComputador);
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        carrinhoCompras.setMaxAge(60 * 60 * 24);
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;
        if (requestCookies != null) {
            for (var c : requestCookies) {
                achouCarrinho = true;
                carrinhoCompras = c;
                break;
            }
        }
        Computador computadores = null;
        if (computador != null){
            computadores = computador;
            if (achouCarrinho == true){
                String value = carrinhoCompras.getValue();
                carrinhoCompras.setValue(value + computadores.getId() + "|");
            }else{
                carrinhoCompras.setValue(computadores.getId() + "|");
            }
        }else {
            response.addCookie(carrinhoCompras);
        }
        response.addCookie(carrinhoCompras);
    }
    @GetMapping("/admin")
    public String getComputadorAdmin(Model model, HttpServletResponse response){

        List<Computador> computador = service.findAll();
        model.addAttribute("computador", computador);

        Cookie cookie = new Cookie("visita","cookie-value");
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return "index";
    }
    @GetMapping("/visualizarCarrinho")
    public String visualizarCarrinho(HttpServletRequest request, Model model) throws ServletException, IOException {
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        Cookie[] requestCookies = request.getCookies();
        boolean achouCarrinho = false;
        if (requestCookies != null) {
            for (var c : requestCookies) {
                achouCarrinho = true;
                carrinhoCompras = c;
                break;
            }
        }
        Computador computador = null;
        var i = 0;
        ArrayList<Computador> lista_computadores = new ArrayList();
        if(achouCarrinho == true) {
            StringTokenizer tokenizer = new StringTokenizer(carrinhoCompras.getValue(), "|");
            while (tokenizer.hasMoreTokens()) {
                computador = service.findById((long) Integer.parseInt(tokenizer.nextToken()));
                lista_computadores.add(computador);
            }
            model.addAttribute("computadores", lista_computadores);
            return "relação";

        } else {
            return "redirect:/index";
        }
    }
    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request, HttpServletResponse response){
        Cookie carrinhoCompras = new Cookie("carrinhoCompras", "");
        response.addCookie(carrinhoCompras);
        return "redirect:/index";
    }
}
