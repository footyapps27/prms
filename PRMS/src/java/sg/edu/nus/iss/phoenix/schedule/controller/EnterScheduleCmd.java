/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.edu.nus.iss.phoenix.schedule.controller;

import at.nocturne.api.Action;
import at.nocturne.api.Perform;
import defaultExceptions.ProgramSlotExistsException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sg.edu.nus.iss.phoenix.radioprogram.delegate.ProgramDelegate;
import sg.edu.nus.iss.phoenix.schedule.delegate.ReviewSelectScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.delegate.ScheduleDelegate;
import sg.edu.nus.iss.phoenix.schedule.entity.ProgramSlot;
import sg.edu.nus.iss.phoenix.user.delegate.UserDelegate;

/**
 *
 * @author linby
 */
@Action("enterprogramslot")
public class EnterScheduleCmd implements Perform {

    @Override
    public String perform(String string, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ScheduleDelegate scheduleDelegate = new ScheduleDelegate();
        UserDelegate userDelegate = new UserDelegate();
        ProgramDelegate programDelegate = new ProgramDelegate();

        ProgramSlot programSlot = new ProgramSlot();

        String dateString = req.getParameter("dateOfProgram");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        programSlot.setStartDateTime(LocalDateTime.parse(dateString, formatter));
        try {
            programSlot.setPresenter(userDelegate.loadUser(req.getParameter("presenter")));
            programSlot.setProducer(userDelegate.loadUser(req.getParameter("producer")));
            programSlot.setRadioProgram(programDelegate.loadRadioProgram(req.getParameter("program")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        String ins = (String) req.getParameter("ins");
        if (ins.equalsIgnoreCase("true")) {
            //insert
            try {
                scheduleDelegate.processCreateProgramSlot(programSlot);
            } catch (ProgramSlotExistsException e) {
                e.printStackTrace();
                req.setAttribute("error", e.getMessage());
                return "/pages/error.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Fatal Exception:" + e.getMessage());
                return "/pages/error.jsp";
            }
        } else {
            //update
            String id = req.getParameter("id");
            programSlot.setId(Integer.valueOf(id));
            try {
                scheduleDelegate.processUpdateProgramSlot(programSlot);
            } catch (ProgramSlotExistsException e) {
                e.printStackTrace();
                req.setAttribute("error", e.getMessage());
                return "/pages/error.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Fatal Exception:" + e.getMessage());
                return "/pages/error.jsp";
            }
        }

        // Navigation
        ReviewSelectScheduleDelegate reviewSelectScheduleDelegate = new ReviewSelectScheduleDelegate();
        try {
            List<ProgramSlot> data = reviewSelectScheduleDelegate.reviewSelectProgramSlot();
            req.setAttribute("scheduleList", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pages/crudschedule.jsp";
    }

}
