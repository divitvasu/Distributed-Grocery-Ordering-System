package com.GroceryOrderingSystem.communication;

import java.rmi.Remote;
import java.util.ArrayList;

public interface Key_Store_Int extends Remote {

    ArrayList remote_put_pair (ArrayList request, int serverno) throws Exception;

}

