package com.teamc.bioskop.Controller.MVC;

import com.teamc.bioskop.Model.*;
import com.teamc.bioskop.Service.SeatsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class MVCSeatsController {

    private final SeatsService seatService;



    @GetMapping("/getseats")
    public String showSeats(Model model){
        return paginatedSeats(1, model);
    }

    @GetMapping("/getseats/{page}")
    public String paginatedSeats(@PathVariable(value="page") int pageNumber, Model model){
        int pageSize = 10;
        Page<Seats> seatsPage = this.seatService.findPaginated(pageNumber, pageSize);
        List<Seats> seatList = seatsPage.getContent();

        model.addAttribute("currentPage",pageNumber);
        model.addAttribute("totalPages",seatsPage.getTotalPages());
        model.addAttribute("totalItems",seatsPage.getTotalElements());
        model.addAttribute("seats",seatList);

        return "seats";
    }

    @GetMapping("/add-seats")
    public String formSeats(Model model) {
        Seats seat = new Seats();
        model.addAttribute("seats", seat);

        return "add-seats";
    }

    @PostMapping("/added-seats")
    public String newSeats(@ModelAttribute("seats") Seats seats) {
        this.seatService.createseat(seats);
        return "dashboard";
    }

    /*
    Update Seats Session
     */
    @GetMapping("/update/seat/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Seats seat = this.seatService.getReferenceById(id);
        model.addAttribute("seat", seat);
        return "update-seat";
    }

    @PostMapping("/update/seat/{id}")
    public String updateById(@PathVariable("id") Long id,
                             @ModelAttribute("seats") Seats seats) {
        seats.setSeatId(id);
        this.seatService.updateseat(seats, id);
        return "redirect:/getseats";
    }

    @GetMapping("/delete/seat/{id}")
    public String deleteSeat(@PathVariable("id") long id, Model model) {
        Seats seats = seatService.findbyid(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + id));
        seatService.deleteseat(id);
        return "redirect:/getseats";
    }

    /*
    Filter Seats Data By Status - Management Page
     */
    @GetMapping("/seats-status")
    public String showSeatsByStudio(@RequestParam(value = "isAvailable", required = false) StatusSeats statusSeats, Model model){
        return paginatedSeatsbyStatus(1, model, statusSeats);
    }

    @GetMapping("/seats-status/{pageStatus}")
    public String paginatedSeatsbyStatus(@PathVariable(value = "pageStatus") int pageNumber, Model model, @RequestParam(value = "isAvailable", required = false) StatusSeats statusSeats){

        int pageSize = 10;

        Page<Seats> seatsPage = this.seatService.findPaginatedByStatus(statusSeats, pageNumber, pageSize);
        List<Seats> seatList = seatsPage.getContent();

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", seatsPage.getTotalPages());
        model.addAttribute("totalItems", seatsPage.getTotalElements());
        model.addAttribute("seats", seatList);

        return "seats";
    }

}