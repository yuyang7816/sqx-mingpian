package com.sqx.modules.utils;

/**
 * @author chengming
 * @since 2026/3/10
 */
public interface DashscopePrompt {

    String companyInfoScheme = "{\n" +
            "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
            "  \"title\": \"CompanyBusinessInfo\",\n" +
            "  \"description\": \"中国企业工商注册信息数据结构定义\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"company_name\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"公司名称\"\n" +
            "    },\n" +
            "    \"unified_social_credit_code\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"统一社会信用代码\",\n" +
            "      \"pattern\": \"^[0-9A-HJ-NPQRTUWXY]{2}\\\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$\"\n" +
            "    },\n" +
            "    \"registration_status\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"登记状态（如：存续、吊销等）\"\n" +
            "    },\n" +
            "    \"legal_representative\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"法定代表人姓名\"\n" +
            "    },\n" +
            "    \"registered_capital\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"注册资本（含单位）\"\n" +
            "    },\n" +
            "    \"establishment_date\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"format\": \"date\",\n" +
            "      \"description\": \"成立日期 (YYYY-MM-DD)\"\n" +
            "    },\n" +
            "    \"business_term\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"经营期限\"\n" +
            "    },\n" +
            "    \"registered_address\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"注册地址\"\n" +
            "    },\n" +
            "    \"business_scope\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"经营范围\"\n" +
            "    },\n" +
            "    \"company_type\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"公司类型\"\n" +
            "    },\n" +
            "    \"registration_authority\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"登记机关\"\n" +
            "    },\n" +
            "    \"paid_in_capital\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"实缴资本（含单位）\"\n" +
            "    },\n" +
            "    \"industry\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"所属行业\"\n" +
            "    },\n" +
            "    \"staff_size\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"人员规模描述\"\n" +
            "    },\n" +
            "    \"website\": {\n" +
            "      \"type\": [\"string\", \"null\"],\n" +
            "      \"format\": \"uri\",\n" +
            "      \"description\": \"公司官网地址，若无则为 null\"\n" +
            "    },\n" +
            "    \"phone\": {\n" +
            "      \"type\": [\"string\", \"null\"],\n" +
            "      \"description\": \"联系电话，若无则为 null\"\n" +
            "    },\n" +
            "    \"email\": {\n" +
            "      \"type\": [\"string\", \"null\"],\n" +
            "      \"format\": \"email\",\n" +
            "      \"description\": \"联系邮箱，若无则为 null\"\n" +
            "    },\n" +
            "    \"risk_info\": {\n" +
            "      \"type\": \"object\",\n" +
            "      \"description\": \"风险信息概览\",\n" +
            "      \"properties\": {\n" +
            "        \"abnormal_operation\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否经营异常\"\n" +
            "        },\n" +
            "        \"serious_violations\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否有严重违法\"\n" +
            "        },\n" +
            "        \"court_announcements\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否有法院公告\"\n" +
            "        },\n" +
            "        \"executed_person\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否为被执行人\"\n" +
            "        },\n" +
            "        \"restricted_consumer\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否限制高消费\"\n" +
            "        },\n" +
            "        \"tax_arrears\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否有欠税\"\n" +
            "        },\n" +
            "        \"administrative_penalties\": {\n" +
            "          \"type\": \"boolean\",\n" +
            "          \"description\": \"是否有行政处罚\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"required\": [\n" +
            "        \"abnormal_operation\",\n" +
            "        \"serious_violations\",\n" +
            "        \"court_announcements\",\n" +
            "        \"executed_person\",\n" +
            "        \"restricted_consumer\",\n" +
            "        \"tax_arrears\",\n" +
            "        \"administrative_penalties\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"shareholder_info\": {\n" +
            "      \"type\": \"array\",\n" +
            "      \"description\": \"股东信息列表\",\n" +
            "      \"items\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"name\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"股东姓名或名称\"\n" +
            "          },\n" +
            "          \"contribution_ratio\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"持股比例\"\n" +
            "          },\n" +
            "          \"contribution_amount\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"认缴出资额\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"required\": [\"name\", \"contribution_ratio\", \"contribution_amount\"]\n" +
            "      }\n" +
            "    },\n" +
            "    \"historical_changes\": {\n" +
            "      \"type\": \"array\",\n" +
            "      \"description\": \"历史变更记录\",\n" +
            "      \"items\": {\n" +
            "        \"type\": \"object\",\n" +
            "        \"properties\": {\n" +
            "          \"change_item\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"变更事项\"\n" +
            "          },\n" +
            "          \"before_change\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"变更前内容\"\n" +
            "          },\n" +
            "          \"after_change\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"description\": \"变更后内容\"\n" +
            "          },\n" +
            "          \"change_date\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"format\": \"date\",\n" +
            "            \"description\": \"变更日期\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"required\": [\"change_item\", \"before_change\", \"after_change\", \"change_date\"]\n" +
            "      }\n" +
            "    },\n" +
            "    \"verification_result\": {\n" +
            "      \"type\": \"string\",\n" +
            "      \"description\": \"核验结论描述\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"required\": [\n" +
            "    \"company_name\",\n" +
            "    \"unified_social_credit_code\",\n" +
            "    \"registration_status\",\n" +
            "    \"legal_representative\",\n" +
            "    \"registered_capital\",\n" +
            "    \"establishment_date\",\n" +
            "    \"registered_address\",\n" +
            "    \"business_scope\",\n" +
            "    \"company_type\",\n" +
            "    \"registration_authority\",\n" +
            "    \"risk_info\",\n" +
            "    \"shareholder_info\",\n" +
            "    \"verification_result\"\n" +
            "  ]\n" +
            "}";

}
