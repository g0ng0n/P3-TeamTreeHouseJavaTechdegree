package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by gonzalo.gisbert on 20/03/17.
 */
public class UserTest {

    private User user;
    private User user2;
    private Board board;
    private User admin;
    private Question question;
    private Answer answer;
    private Answer answer2;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        board = new Board("Maths");
        user = new User(board, "user1");
        user2 = new User(board, "user2");
        admin = new User(board, "admin1");
        question = new Question(user, "Q1");
        answer = new Answer(question, user, "Answer1");
        answer2 = new Answer(question, admin, "Answer1");

    }


    @Test
    public void questionerReputationGoesUpByFivePoints(){

        board.addQuestion(question);
        admin.upVote(question);

        assertEquals(5, user.getReputation());
    }

    @Test
    public void answererReputationGoesUpByTenPoints(){

        board.addAnswer(answer);
        admin.upVote(answer);

        assertEquals(10, user.getReputation());
    }

    @Test
    public void acceptedAnswerReputationGoesUpByFifteenPoints(){

        answer.setAccepted(true);
        board.addAnswer(answer);

        assertEquals(15, user.getReputation());
    }

    @Test
    public void invalidUpVoteOrDownVoteWithTheSameUser(){
        thrown.expect(VotingException.class);
        thrown.expectMessage("You cannot vote for yourself!");

        user.upVote(question);
        user.downVote(question);

    }

    @Test
    public void onlyTheOriginalQuestionerCanAcceptAnAnswer(){
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(String.format("Only %s can accept this answer as it is their question",
                user.getName()));

        user2.acceptAnswer(answer);

    }

    @Test
    public void userAcceptAnswer(){
        user.acceptAnswer(answer);

        assertEquals(true, answer.isAccepted());
    }


}