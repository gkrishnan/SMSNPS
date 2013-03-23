package com.gkrishnan.smsnps;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

public class VoteDB {
	static Logger logger = Logger.getLogger(VoteDB.class.getName());
	public static boolean saveVoteInfoEntry(String mobile, String rank) {
		logger.log(Level.WARNING,"Inside VoteDB");
		VoteInfo pollInfo = new VoteInfo(mobile, rank);
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.makePersistent(pollInfo);
		logger.log(Level.WARNING,"Returning after normal persistence");
		return true;
	}
}
