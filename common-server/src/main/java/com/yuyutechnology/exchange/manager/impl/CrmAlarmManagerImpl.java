package com.yuyutechnology.exchange.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.dao.CrmAlarmDAO;
import com.yuyutechnology.exchange.dao.CrmSupervisorDAO;
import com.yuyutechnology.exchange.dao.CrmUserInfoDAO;
import com.yuyutechnology.exchange.dao.TransferDAO;
import com.yuyutechnology.exchange.dto.NotifyWithdrawDTO;
import com.yuyutechnology.exchange.enums.ConfigKeyEnum;
import com.yuyutechnology.exchange.mail.MailManager;
import com.yuyutechnology.exchange.manager.ConfigManager;
import com.yuyutechnology.exchange.manager.CrmAlarmManager;
import com.yuyutechnology.exchange.manager.OandaRatesManager;
import com.yuyutechnology.exchange.pojo.CrmAlarm;
import com.yuyutechnology.exchange.pojo.CrmSupervisor;
import com.yuyutechnology.exchange.sms.SmsManager;
import com.yuyutechnology.exchange.util.DateFormatUtils;

@Service
public class CrmAlarmManagerImpl implements CrmAlarmManager {

	private static Logger logger = LogManager.getLogger(CrmAlarmManagerImpl.class);
	@Autowired
	CrmAlarmDAO crmAlarmDAO;
	@Autowired
	CrmSupervisorDAO crmSupervisorDAO;
	@Autowired
	CrmUserInfoDAO crmUserInfoDAO;
	@Autowired
	TransferDAO transferDAO;

	@Autowired
	ConfigManager configManager;
	@Autowired
	SmsManager smsManager;
	@Autowired
	MailManager mailManager;

	@Autowired
	OandaRatesManager oandaRatesManager;

	@Override
	public void addAlarmConfig(int alarmType, BigDecimal lowerLimit, BigDecimal upperLimit, int alarmMode, int editorid,
			String supervisorId) {
		CrmAlarm crmAlarm = new CrmAlarm();

		crmAlarm.setAlarmType(alarmType);
		crmAlarm.setAlarmMode(alarmMode);
		crmAlarm.setCreateAt(new Date());
		crmAlarm.setLowerLimit(lowerLimit);
		crmAlarm.setUpperLimit(upperLimit);
		crmAlarm.setSupervisorIdArr(supervisorId);
		crmAlarm.setEditorid(editorid);
		crmAlarm.setAlarmAvailable(1);

		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);

	}

	@Override
	public void alarmNotice(String supervisorIdArr, String features, int earlyWarningMode,
			HashMap<String, Object> params) {
		String[] arr = (supervisorIdArr.replace("[", "").replace("]", "")).split(",");
		switch (features) {
		case "reserveEarlyWarning":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(),
								new BigDecimal(params.get("difference").toString()),
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(),
								new BigDecimal(params.get("difference").toString()),
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4criticalAlarm(crmSupervisor.getSupervisorEmail(),
								new BigDecimal(params.get("difference").toString()),
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
						smsManager.sendSMS4CriticalAlarm(crmSupervisor.getSupervisorMobile(),
								new BigDecimal(params.get("difference").toString()),
								new BigDecimal(params.get("lowerLimit").toString()),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}

			break;
		case "largeTransactionWarning":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4LargeTrans(crmSupervisor.getSupervisorMobile(),
								params.get("payerMobile").toString(), params.get("payeeMobile").toString(),
								new BigDecimal(params.get("amount").toString()), params.get("currency").toString(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4LargeTrans(crmSupervisor.getSupervisorEmail(),
								params.get("payerMobile").toString(), params.get("payeeMobile").toString(),
								new BigDecimal(params.get("amount").toString()), params.get("currency").toString(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4LargeTrans(crmSupervisor.getSupervisorEmail(),
								params.get("payerMobile").toString(), params.get("payeeMobile").toString(),
								new BigDecimal(params.get("amount").toString()), params.get("currency").toString(),
								DateFormatUtils.formatDateGMT8(new Date()));
						smsManager.sendSMS4LargeTrans(crmSupervisor.getSupervisorMobile(),
								params.get("payerMobile").toString(), params.get("payeeMobile").toString(),
								new BigDecimal(params.get("amount").toString()), params.get("currency").toString(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}

			break;

		case "largeExchangeWarning":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4LargeExchange(crmSupervisor.getSupervisorMobile(),
								params.get("payerMobile").toString(), DateFormatUtils.formatDateGMT8(new Date()),
								new BigDecimal(params.get("amountOut").toString()),
								params.get("currencyOut").toString(), new BigDecimal(params.get("amountIn").toString()),
								params.get("currencyIn").toString());
					}

				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4LargeExchange(crmSupervisor.getSupervisorEmail(),
								params.get("payerMobile").toString(), DateFormatUtils.formatDateGMT8(new Date()),
								new BigDecimal(params.get("amountOut").toString()),
								params.get("currencyOut").toString(), new BigDecimal(params.get("amountIn").toString()),
								params.get("currencyIn").toString());
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4LargeExchange(crmSupervisor.getSupervisorEmail(),
								params.get("payerMobile").toString(), DateFormatUtils.formatDateGMT8(new Date()),
								new BigDecimal(params.get("amountOut").toString()),
								params.get("currencyOut").toString(), new BigDecimal(params.get("amountIn").toString()),
								params.get("currencyIn").toString());
						smsManager.sendSMS4LargeExchange(crmSupervisor.getSupervisorMobile(),
								params.get("payerMobile").toString(), DateFormatUtils.formatDateGMT8(new Date()),
								new BigDecimal(params.get("amountOut").toString()),
								params.get("currencyOut").toString(), new BigDecimal(params.get("amountIn").toString()),
								params.get("currencyIn").toString());
					}
				}
			}

			break;

		// case "goldpayRemitFailWarning":
		// // 短信
		// if (earlyWarningMode == 1) {
		// for (String supervisorId : arr) {
		// CrmSupervisor crmSupervisor =
		// crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
		// if (crmSupervisor != null) {
		// smsManager.sendSMS4RemitFail(crmSupervisor.getSupervisorMobile(),
		// DateFormatUtils.formatDateGMT8(new Date()));
		// }
		// }
		// }
		// // 邮件
		// if (earlyWarningMode == 2) {
		// for (String supervisorId : arr) {
		// CrmSupervisor crmSupervisor = crmSupervisorDAO
		// .getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
		// if (crmSupervisor != null) {
		// mailManager.mail4RemitFail(crmSupervisor.getSupervisorEmail(),
		// DateFormatUtils.formatDateGMT8(new Date()));
		// }
		// }
		// }
		// // 短信邮件
		// if (earlyWarningMode == 3) {
		// for (String supervisorId : arr) {
		// CrmSupervisor crmSupervisor =
		// crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
		// if (crmSupervisor != null) {
		// mailManager.mail4RemitFail(crmSupervisor.getSupervisorEmail(),
		// DateFormatUtils.formatDateGMT8(new Date()));
		// smsManager.sendSMS4RemitFail(crmSupervisor.getSupervisorMobile(),
		// DateFormatUtils.formatDateGMT8(new Date()));
		// }
		// }
		// }
		//
		// break;

		case "badAccountWarning":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4BadAccount(crmSupervisor.getSupervisorMobile(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4BadAccount(crmSupervisor.getSupervisorEmail(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4BadAccount(crmSupervisor.getSupervisorEmail(),
								DateFormatUtils.formatDateGMT8(new Date()));
						smsManager.sendSMS4BadAccount(crmSupervisor.getSupervisorMobile(),
								DateFormatUtils.formatDateGMT8(new Date()));
					}
				}
			}

			break;
		case "registrationAlarm":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4Registration(crmSupervisor.getSupervisorMobile(),
								DateFormatUtils.formatDateGMT8(new Date()), (String) params.get("registrationNumber"));
					}
				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4Registration(crmSupervisor.getSupervisorEmail(),
								DateFormatUtils.formatDateGMT8(new Date()), (String) params.get("registrationNumber"));
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4Registration(crmSupervisor.getSupervisorEmail(),
								DateFormatUtils.formatDateGMT8(new Date()), (String) params.get("registrationNumber"));
						smsManager.sendSMS4Registration(crmSupervisor.getSupervisorMobile(),
								DateFormatUtils.formatDateGMT8(new Date()), (String) params.get("registrationNumber"));
					}
				}
			}

			break;
		case "reachTotalGQDLimtWarning":
			// 短信
			if (earlyWarningMode == 1) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						smsManager.sendSMS4ReachTotalGDQLimit(crmSupervisor.getSupervisorMobile(),
								(String) params.get("totalGDQCanBeSold"), (String) params.get("percent"));
					}
				}
			}
			// 邮件
			if (earlyWarningMode == 2) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO
							.getCrmSupervisorById(Integer.parseInt(supervisorId.trim()));
					if (crmSupervisor != null) {
						mailManager.mail4ReachTotalGDQLimit(crmSupervisor.getSupervisorEmail(),
								(String) params.get("totalGDQCanBeSold"), (String) params.get("percent"));
					}
				}
			}
			// 短信邮件
			if (earlyWarningMode == 3) {
				for (String supervisorId : arr) {
					CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
					if (crmSupervisor != null) {
						mailManager.mail4ReachTotalGDQLimit(crmSupervisor.getSupervisorEmail(),
								(String) params.get("totalGDQCanBeSold"), (String) params.get("percent"));
						smsManager.sendSMS4ReachTotalGDQLimit(crmSupervisor.getSupervisorMobile(),
								(String) params.get("totalGDQCanBeSold"), (String) params.get("percent"));
					}
				}
			}

			break;

		default:
			break;
		}
	}

	@Override
	public int delAlarmConfig(int alarmId) {
		logger.info("delete alarmId : {}", alarmId);
		return crmAlarmDAO.delCrmAlarmConfig(alarmId);
	}

	@Override
	public void delSupervisorById(Integer supervisorId) {
		crmSupervisorDAO.delSupervisorById(supervisorId);
	}

	@Override
	public CrmAlarm getAlarmConfigById(int alarmId) {
		return crmAlarmDAO.getCrmAlarmConfig(alarmId);
	}

	@Override
	public List<CrmAlarm> getCrmAlarmConfigList() {
		return crmAlarmDAO.getCrmAlarmConfigList();
	}

	@Override
	public List<CrmSupervisor> getCrmSupervisorList() {
		return crmSupervisorDAO.getCrmSupervisorList();
	}

	@Override
	public HashMap<String, BigDecimal> getLargeTransLimit() {
		HashMap<String, BigDecimal> map = new HashMap<>();

		BigDecimal transferLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.TRANSFERLIMITPERPAY, 10000d));
		BigDecimal exchangeLimitPerPay = BigDecimal
				.valueOf(configManager.getConfigDoubleValue(ConfigKeyEnum.EXCHANGELIMITPERPAY, 100000d));

		map.put("transferLimitPerPay", transferLimitPerPay);
		map.put("exchangeLimitPerPay", exchangeLimitPerPay);

		return map;
	}

	@Override
	public void reachtotalGDQLimitAlarm(final BigDecimal accumulatedAmount, final BigDecimal percent) {
		List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(4, 1);
		if (list == null) {
			logger.warn("No related alarm information is configured ! {}", new Date());
			return;
		}
		for (final CrmAlarm crmAlarm : list) {
			if ((percent.multiply(new BigDecimal("100"))).compareTo(crmAlarm.getLowerLimit()) >= 0) {

				logger.info("Initiate an alarm, alarmId : {},alarmMode: {}",
						new Object[] { crmAlarm.getAlarmId(), crmAlarm.getAlarmMode() });

				alarmNotice(crmAlarm.getSupervisorIdArr(), "reachTotalGQDLimtWarning", crmAlarm.getAlarmMode(),
						new HashMap<String, Object>() {
							/**
							 * 
							 */
							private static final long serialVersionUID = 1L;

							{
								put("totalGDQCanBeSold", accumulatedAmount.toString());
								put("percent", (percent.multiply(new BigDecimal("100"))).toString());
							}
						});
				// 生成警报记录
			}
		}

	}

	// @Override
	// public HashMap<String, BigDecimal> getAccountInfo(BigDecimal
	// userHoldingTotalAssets) {
	//
	// HashMap<String, BigDecimal> map = new HashMap<>();
	//
	// // Ex公司持有的总资产 = 用户充值Goldpay总额 - 用户提现Goldpay总额;
	// // 预备金剩余量 = 1 - (Ex用户持有的总资产 - Ex公司持有的总资产) / 预备金;
	// BigDecimal sumRecharge =
	// transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_IN_GOLDPAY_RECHARGE);
	// BigDecimal sumWithdraw =
	// transferDAO.sumGoldpayTransAmount(ServerConsts.TRANSFER_TYPE_OUT_GOLDPAY_WITHDRAW);
	// BigDecimal exHoldingTotalAssets =
	// oandaRatesManager.getDefaultCurrencyAmount(ServerConsts.CURRENCY_OF_GOLDPAY,
	// sumRecharge.subtract(sumWithdraw));
	//
	// logger.info("sumRecharge:{},sumWithdraw:{},exHoldingTotalAssets(USD):{}",
	// new Object[] { sumRecharge, sumWithdraw, exHoldingTotalAssets });
	//
	// String reserveFundsStr =
	// configManager.getConfigStringValue(ConfigKeyEnum.RESERVEFUNDS, "100000000");
	// BigDecimal reserveFunds =
	// oandaRatesManager.getDefaultCurrencyAmount(ServerConsts.CURRENCY_OF_GOLDPAY,
	// new BigDecimal(reserveFundsStr));
	//
	// logger.info("reserveFunds (USD) :{}", reserveFunds);
	//
	// BigDecimal reserveAvailability = BigDecimal.ZERO;
	//
	// if (reserveFunds.compareTo(BigDecimal.ZERO) != 0) {
	// reserveAvailability = (BigDecimal.ONE
	// .subtract((userHoldingTotalAssets.subtract(exHoldingTotalAssets.setScale(4,
	// RoundingMode.DOWN)))
	// .divide(reserveFunds, 5, RoundingMode.DOWN))).multiply(new
	// BigDecimal("100"));
	// }
	// logger.info("userHoldingTotalAssets:{} ", userHoldingTotalAssets);
	// logger.info("exHoldingTotalAssets:{} ", exHoldingTotalAssets);
	// logger.info("reserveFunds:{}", reserveFunds);
	// logger.info("ReserveAvailability : {}%", reserveAvailability);
	//
	// map.put("exHoldingTotalAssets", exHoldingTotalAssets.setScale(4,
	// RoundingMode.DOWN));
	// map.put("userHoldingTotalAssets", userHoldingTotalAssets);
	// map.put("reserveFunds", reserveFunds.setScale(4, RoundingMode.DOWN));
	// map.put("reserveAvailability", reserveAvailability.setScale(2,
	// RoundingMode.DOWN));
	//
	// return map;
	// }
	//
	// @SuppressWarnings("serial")
	// @Override
	// public void autoAlarm(BigDecimal userHoldingTotalAssets) {
	//
	// final HashMap<String, BigDecimal> map =
	// getAccountInfo(userHoldingTotalAssets);
	//
	// if (map == null) {
	// return;
	// }
	//
	// List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(0, 1);
	//
	// if (list == null) {
	// logger.warn("No related alarm information is configured ! {}", new Date());
	// return;
	// }
	//
	// for (final CrmAlarm crmAlarm : list) {
	// if ((crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == 0
	// || crmAlarm.getLowerLimit().compareTo(map.get("reserveAvailability")) == -1)
	// && (crmAlarm.getUpperLimit().compareTo(map.get("reserveAvailability")) == 1))
	// {
	//
	// logger.info("Initiate an alarm, alarmId : {},alarmMode: {}",
	// new Object[] { crmAlarm.getAlarmId(), crmAlarm.getAlarmMode() });
	//
	// alarmNotice(crmAlarm.getSupervisorIdArr(), "reserveEarlyWarning",
	// crmAlarm.getAlarmMode(),
	// new HashMap<String, Object>() {
	// {
	// put("difference", map.get("reserveAvailability"));
	// put("lowerLimit", crmAlarm.getLowerLimit());
	// }
	// });
	// // 生成警报记录
	// }
	// }
	// }

	@Override
	public void registrationAlarm() {
		Long registrationNumber = crmUserInfoDAO.get24HRegistration();
		if (registrationNumber > configManager.getConfigLongValue(ConfigKeyEnum.REGISTRATION_WARN, 100L)) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("registrationNumber", registrationNumber.toString());
			List<CrmAlarm> list = crmAlarmDAO.getConfigListByTypeAndStatus(5, 1);
			if (list != null && !list.isEmpty()) {
				logger.info("registrationAlarm listSize: {}", list.size());
				for (int i = 0; i < list.size(); i++) {
					CrmAlarm crmAlarm = list.get(i);
					logger.info("registrationAlarm crmAlarm: {}", crmAlarm.getSupervisorIdArr());
					alarmNotice(crmAlarm.getSupervisorIdArr(), "registrationAlarm", crmAlarm.getAlarmMode(), params);
				}
			}
		}
	}

	@Override
	public String saveSupervisor(String supervisorName, String supervisorMobile, String supervisorEmail) {

		List<CrmSupervisor> list = crmSupervisorDAO.getCrmSupervisorByCondition(supervisorName, supervisorMobile,
				supervisorEmail);

		if (list == null || list.isEmpty()) {
			CrmSupervisor crmSupervisor = new CrmSupervisor();
			crmSupervisor.setSupervisorName(supervisorName);
			crmSupervisor.setSupervisorMobile(supervisorMobile);
			crmSupervisor.setSupervisorEmail(supervisorEmail);
			crmSupervisor.setUpdateAt(new Date());

			crmSupervisorDAO.saveCrmSupervisor(crmSupervisor);

			return "success";
		}
		return "fail";

	}

	@Override
	public int updateAlarmAvailable(Integer alarmId, int alarmAvailable) {
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmAvailable(alarmAvailable);
		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
		return crmAlarm.getAlarmType();
	}

	@Override
	public void updateAlarmConfig(Integer alarmId, int alarmType, BigDecimal lowerLimit, BigDecimal upperLimit,
			int alarmMode, int editorid, String adminIdArr) {
		CrmAlarm crmAlarm = crmAlarmDAO.getCrmAlarmConfig(alarmId);
		crmAlarm.setAlarmId(alarmId);
		crmAlarm.setAlarmType(alarmType);
		crmAlarm.setLowerLimit(lowerLimit);
		crmAlarm.setUpperLimit(upperLimit);
		crmAlarm.setAlarmMode(alarmMode);
		crmAlarm.setSupervisorIdArr(adminIdArr);
		crmAlarm.setEditorid(editorid);

		crmAlarmDAO.addCrmAlarmConfig(crmAlarm);
	}

	@Override
	public void notifyWithdraw(NotifyWithdrawDTO notifyWithdrawDTO) {
		List<CrmAlarm> crmAlarms = crmAlarmDAO.getConfigListByTypeAndStatus(ServerConsts.ALARM_TYPE_WITHDRAW, 1);
		if (crmAlarms == null || crmAlarms.isEmpty()) {
			return;
		}
		for (CrmAlarm crmAlarm : crmAlarms) {
			String[] supervisorIdArr = (crmAlarm.getSupervisorIdArr().replace("[", "").replace("]", "")).split(",");
			List<String> toMails = new ArrayList<>();
			List<String> toPhones = new ArrayList<>();
			
			for (String supervisorId : supervisorIdArr) {
				CrmSupervisor crmSupervisor = crmSupervisorDAO.getCrmSupervisorById(Integer.parseInt(supervisorId));
				toMails.add(crmSupervisor.getSupervisorEmail());
				toPhones.add(crmSupervisor.getSupervisorMobile());
			}

			switch (crmAlarm.getAlarmMode()) {
			case ServerConsts.ALARM_MODE_SMS:
				for (String phone : toPhones) {
					smsManager.sendSMS4NotifyWithdray(phone, notifyWithdrawDTO);
				}
				break;
			case ServerConsts.ALARM_MODE_EMAIL:
				mailManager.mail4NotifyWithdray(toMails, notifyWithdrawDTO);
				break;
			case ServerConsts.ALARM_MODE_SMS_AND_EMAIL:
				for (String phone : toPhones) {
					smsManager.sendSMS4NotifyWithdray(phone, notifyWithdrawDTO);
				}
				mailManager.mail4NotifyWithdray(toMails, notifyWithdrawDTO);
				break;
			default:
				break;
			}
		}
	}
}
