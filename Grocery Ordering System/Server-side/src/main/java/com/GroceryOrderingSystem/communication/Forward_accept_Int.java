package com.GroceryOrderingSystem.communication;

import java.rmi.Remote;
import java.util.ArrayList;

public interface Forward_accept_Int extends Remote {
    ArrayList remote_forward(ArrayList request) throws Exception;
}
