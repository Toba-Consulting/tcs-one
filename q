[33mcommit 162ea592251e3f6523f6003c5b240072af6a4b0b[m[33m ([m[1;36mHEAD -> [m[1;32mmaster[m[33m)[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Tue Nov 22 08:36:56 2022 +0700

    add organization as filter to create lines from requisition on PO window

[33mcommit 32f3a466cac6f8bbcd9fba84091cb7f2f2618f5c[m[33m ([m[1;31morigin/master[m[33m)[m
Merge: 4af7077 7388e39
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Tue Nov 22 08:32:05 2022 +0700

    Merge branch 'master' of https://github.com/Toba-Consulting/tcs-one

[33mcommit 4af7077edc8a8ba27fab5bf9354fb8ff43978cd7[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Tue Nov 22 08:31:04 2022 +0700

    remove custom HWH code

[33mcommit c2873bb1cc7d0615fbf1749da9660576c6477c5a[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Tue Nov 22 08:29:50 2022 +0700

    housekeeping callout files

[33mcommit 7388e39df3c9adc53880d93c3bdca75d3c3f7034[m
Author: edwinang003 <edwin@tobaconsulting.com>
Date:   Wed Nov 16 17:32:13 2022 +0700

    fix merge conflict

[33mcommit 8d218bad41d23220e18bc97c9cbceb885be1cc0f[m
Merge: 6ec4fad 5b69658
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Wed Nov 16 17:19:16 2022 +0700

    Merge remote-tracking branch 'origin/dev-mitraabadi'
    
    # Conflicts:
    #       id.tcs.module/src/id/tcs/process/CreateAssetFromAssetAddition.java
    #       id.tcs.module/src/org/compiere/acct/TCS_Doc_AssetAddition.java
    #       id.tcs.module/src/org/compiere/process/HWH_BOMExplosion.java

[33mcommit 6ec4fadc757e66d5303efa7bc24a74b033caac4b[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Wed Nov 16 11:04:18 2022 +0700

    add process to calculate promotion

[33mcommit 1d535af3b4edb776c9037e71415ae43083b3d895[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Wed Nov 16 11:03:55 2022 +0700

    fix to inbound outbound process

[33mcommit fea6023aeabc5e2d00e87ec1fc1bcabd58d7e582[m
Author: Edwin Ang <edwin.ang.003@gmail.com>
Date:   Wed Nov 16 11:03:16 2022 +0700

    various small fix

[33mcommit 5b69658146015f982158ac9d8b4193ef310f246d[m[33m ([m[1;31morigin/dev-mitraabadi[m[33m)[m
Author: edwinang003 <edwin@tobaconsulting.com>
Date:   Thu Nov 3 16:42:33 2022 +0700

    add reactive option when docstatus is waitingpayment on c_order

[33mcommit 76e5c0c9dbd21f0af6a99cf146d7c1f6afa91f6b[m
Author: edwinang003 <edwin@tobaconsulting.com>
Date:   Wed Nov 2 15:56:21 2022 +0700

    enhance bank transfer and adjustment to mitraabadi

[33mcommit 73bb69d0575581cba337f5057db79e9dce1e4003[m
Author: edwinang003 <edwin@tobaconsulting.com>
Date:   Fri Oct 28 19:29:18 2022 +0700

    develop for mitraabadi

[33mcommit 0aa38e0f552ba878a1bdc34da43231db2b2d9203[m
Merge: 06e6ea6 9824615
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jun 29 19:50:26 2020 +0700

    Merge with stabilize

[33mcommit 06e6ea67611d9483529ef41f67c4bc10666ed164[m
Merge: 696ab11 0f57029
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jun 29 19:50:14 2020 +0700

    Merge

[33mcommit 696ab11e18bc2668fe3607af14404660a39a78a6[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jun 29 19:50:05 2020 +0700

    fix periodic cost code

[33mcommit 35d5fee6c48bb5ce32674bac0cfdb8adae5900df[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Sat Jun 27 17:36:51 2020 +0700

    commit

[33mcommit 0f570298ae3ea0f6b992414d57f3938a8ef796df[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Fri Jun 26 21:22:55 2020 +0700

    updated HWH_BOMExplosion

[33mcommit 6603fac1147d910229045ddff55b6b592797f762[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Fri Jun 26 14:19:31 2020 +0700

    add filter product as component on select top level FG in BOM Explosion

[33mcommit 0f552a58ffc7f5374da62b9ccf3755960266a640[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Fri Jun 26 13:55:09 2020 +0700

    checkpoint

[33mcommit 9824615b2df858b4cb5cb75060bc5b4f568f94ac[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Wed Jun 17 14:59:25 2020 +0700

    changed orderby using code not query because of absolute, and removed setdescription using header data

[33mcommit 5a97170b3522b3d078a28bee687bdd703305a976[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Tue Jun 16 14:01:47 2020 +0700

    added orderby in charge query to match orderby in payment amount, and fixing query on date range parameter

[33mcommit 649de185280acad400da2ad6fbc6fea8917da34d[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Jun 16 13:29:12 2020 +0700

    init commit periodic cost for HWH

[33mcommit 9476ee3a0195be5b16569ac703ffb9db8dde37ca[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Fri Jun 12 20:06:02 2020 +0700

    updated tcs match allocation

[33mcommit f17e59b942e811a14e675e447ffcd8bcb9370939[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Fri Jun 12 16:39:38 2020 +0700

    fixing tcs match allocation

[33mcommit a39dae630feaa7117829755419604c06736833da[m
Merge: 5d59547 5dd4a87
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue May 26 16:01:04 2020 +0700

    Merge

[33mcommit 5d59547eaf94bdff25fa976ae300a40eaffc8f0e[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Mon Apr 27 16:07:31 2020 +0700

    added aftersave in MQuotation to make c_tax_id in quotation and quotation line same

[33mcommit 35d22cc3775ee35e4d24584bc7de804aaaf953a6[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Wed Apr 22 15:57:52 2020 +0700

    added callout to set UseLifeYears in Asset Addition after selecting Asset

[33mcommit 823852101d435291186da3df13aa8c56f90550bd[m
Author: edwin <edwin@tobaconsulting.com>
Date:   Thu Mar 26 16:25:42 2020 +0700

    TCS_InterWHCreateInbound check already inbounded, add condition DocStatus NOT IN 'VO' & 'RE'

[33mcommit 5dd4a875b1a07f0c28f3cd9cc19573dadee439a0[m
Merge: f1dda1a c366361
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Fri Mar 13 18:38:14 2020 +0700

    Merge with Custom Match Allocation

[33mcommit c36636172e893782a11655a38d10292515426222[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Fri Mar 13 18:35:47 2020 +0700

    use absolute amount for allocated amt  in NewGenerateMatchAllocation

[33mcommit 9de9199ad7687cf5837aa051b03ce6b9c7424c25[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Mar 11 19:26:17 2020 +0700

    refine NewGenerateMatchAllocation

[33mcommit 8e0f6348ef399aee0d4bff4dba477ab8dea50a90[m
Author: noel <devnull@localhost>
Date:   Fri Mar 6 16:14:39 2020 +0700

    add new callout for pp_product_bom_line

[33mcommit 054d8ef3696f9651358904127cd5a874cfefb7c5[m
Author: noel <devnull@localhost>
Date:   Fri Mar 6 15:55:13 2020 +0700

    add new validator to set uom on pp_product_bomline

[33mcommit f56731f4ca18b6833fb0a734d94f0a4907d092f3[m
Author: noel <devnull@localhost>
Date:   Fri Mar 6 13:53:36 2020 +0700

    changes parameter name from AD_Org_ID to P_AD_Org_ID, to fix issue where general ledger report cannot display when the selected org is summary

[33mcommit f1dda1ae78feba2006ee640200fda986701550dd[m
Merge: c408876 d1e8570
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Mar 4 08:59:39 2020 +0700

    Merge

[33mcommit c40887659a5e022f3f378fd45f77215e464ae650[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Mar 4 08:57:57 2020 +0700

    fix multi org reporting on TCSGeneralLedgerView

[33mcommit f0ab998d5feaece81b7a435618a4d6d734144644[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Mar 4 08:55:40 2020 +0700

    redevelop match allocation solution

[33mcommit 8897065c18403c40237af1978f7d168c138b0975[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Wed Feb 12 16:05:18 2020 +0700

    set bankstatement reactivate posted = N

[33mcommit 432ee3e000dfc22d9c495e9072c49d263cc68487[m
Author: noel <devnull@localhost>
Date:   Wed Feb 5 14:38:10 2020 +0700

    changes the way to set allocation line ad_org_id from bank account

[33mcommit 9575d817d52ca0fbc4341441a4e5a81b66e490af[m
Author: noel <devnull@localhost>
Date:   Wed Feb 5 12:10:02 2020 +0700

    changes the way to set paymentfrom and paymentto ad_org_id from bank account

[33mcommit 7292ebe23a8994a616c6c44db3467c65f3745d64[m
Author: David <devnull@localhost>
Date:   Wed Feb 5 11:07:02 2020 +0700

    TCS_DDOrderCreateFromCOrder.getOrderData add check valid docStatus in QtyUsed query for filterin lines

[33mcommit 8450a5f57f698473f9bdafd6b8024f99e88ce874[m
Author: noel <devnull@localhost>
Date:   Wed Jan 29 15:33:46 2020 +0700

    commit add process before void, delete match quotation

[33mcommit 9f71c420e7e542052aeccde6caad7380a576145e[m
Author: David <devnull@localhost>
Date:   Tue Jan 21 17:09:59 2020 +0700

    TCS_DDOrderCreateFromCOrder change displayed data order

[33mcommit bb2a7dcf467b3b175af754f9b381f0c387649ac6[m
Author: David <devnull@localhost>
Date:   Tue Jan 21 17:09:25 2020 +0700

    TCS_InterWHCreateInbound if fail to complete inbound throw error

[33mcommit 759c9701c8999af1305825c7e8f5967eb73681a8[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Tue Jan 21 14:17:45 2020 +0700

    added process to generate match allocation

[33mcommit e24eb2afc169ab625663bff29e0cb88842faeadb[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Tue Jan 21 14:17:24 2020 +0700

    added TCS_Match_Allocation I and X model class

[33mcommit ff51895e198f81662bc2817b28f3ade3ef79073d[m
Author: Ahmad Iqbal <devnull@localhost>
Date:   Tue Jan 21 14:16:55 2020 +0700

    added validation to payment allocation, can only select 2 row, if 2 row are the same invoice or payment, one of them need to have positive number in open amount

[33mcommit 18d608b9d314e960a86a5ab157b9e0d318bd2e74[m
Author: David <devnull@localhost>
Date:   Fri Jan 17 15:19:06 2020 +0700

    TCS_DDOrderCreateFromCOrder move column

[33mcommit 511d6592622d594abe634badbcce13d7c7e8456b[m
Author: David <devnull@localhost>
Date:   Fri Jan 17 15:18:15 2020 +0700

    TCS_InterWHCreateInbound add validation if outbound in parameter already has been inbounded

[33mcommit cdc7102404f3704a1aec8536725543ada7fec6c6[m
Author: David <devnull@localhost>
Date:   Wed Jan 15 21:39:19 2020 +0700

    TCS_DDOrderCreateFromCOrder change column order and name

[33mcommit d1e8570d035649ed7f9d05757d7dcb8babe42f08[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jan 15 18:59:10 2020 +0700

    add amount condition to get description from tcs_allocatecharge for t_match_allocation

[33mcommit b1a5e2d71d22cc2254c414c361fde7138615eff9[m
Author: David <devnull@localhost>
Date:   Tue Jan 14 13:58:13 2020 +0700

    DD_Order CreateFrom C_Order

[33mcommit b2528156a4db20537d254016bf83927b1a628de3[m
Author: David <devnull@localhost>
Date:   Tue Jan 14 13:57:28 2020 +0700

    DDOrder create Outbound, set Qty to Qty not yet oubounded

[33mcommit 2a94a329320cf33093cd0926a037757691627ead[m
Author: David <devnull@localhost>
Date:   Tue Jan 14 13:53:22 2020 +0700

    TCS_MPayment.CompleteIt case AllocateCharge, set TCS_AllocateCharge_ID to Allocation Line

[33mcommit da17f62e823e4d61caed7ae109f55e7ca36bddd9[m
Author: David <devnull@localhost>
Date:   Tue Jan 14 13:52:11 2020 +0700

    MDDORder.Reactivate set lines.QtyReserved=0

[33mcommit 9f5c911969db20dd53f2b7e7906ea42b2b22da04[m
Author: noel <devnull@localhost>
Date:   Thu Jan 9 16:39:53 2020 +0700

    add validation for movementdate parameter

[33mcommit cbd5c56be2ee0ce2c90af4a8fbcd8626534cc3da[m
Author: noel <devnull@localhost>
Date:   Thu Jan 9 15:55:19 2020 +0700

    revert movementdate parameter

[33mcommit 937885771512c3cee4c6a2ec9055ddbab2423c7f[m
Author: noel <devnull@localhost>
Date:   Mon Jan 6 13:22:21 2020 +0700

    commit process to create allocation from payment(allocate to charge)

[33mcommit c0019644a4766232ec98de5c7656e65762e0feb4[m
Author: noel <devnull@localhost>
Date:   Sun Jan 5 19:03:25 2020 +0700

    commit new process to set isbom = Y and create BOM for iron product

[33mcommit 5f36bdc3252325aeef4d7244e05e64cbe32145bb[m
Author: noel <devnull@localhost>
Date:   Thu Jan 2 17:16:27 2020 +0700

    add asset group as use life years trigger

[33mcommit 458454737c523fb6b3f377ee48e04d0e631011ef[m
Author: noel <devnull@localhost>
Date:   Tue Dec 31 09:46:28 2019 +0700

    commit discount callout

[33mcommit 67b98f2a6bfc5a0fae295a1bcb67b905bad2cdb3[m
Author: noel <devnull@localhost>
Date:   Wed Dec 25 14:33:32 2019 +0700

    set some fields at inbound from outbound and set qty outbound and qty inbound

[33mcommit 553fbecc7da77101526274dd9bf24d1386f69769[m
Author: noel <devnull@localhost>
Date:   Wed Dec 18 10:46:16 2019 +0700

    commit changes the way to get the description

[33mcommit 7ccf59857d9dee78f59969a7e031b79573c14ba7[m
Author: David <devnull@localhost>
Date:   Tue Dec 10 15:03:50 2019 +0700

    TCS_MBankStatement.Reactivate check StatementDate period open

[33mcommit 826078fa798c05b496dd055b1e29dc173a83aa97[m
Author: David <devnull@localhost>
Date:   Tue Dec 10 15:03:17 2019 +0700

    Register MatchQuotation MClass to model factory

[33mcommit 05897e53de1e1bd7dddd1937f1b76cfe84e144e1[m
Author: David <devnull@localhost>
Date:   Tue Dec 10 11:36:38 2019 +0700

    Update create quotationline from inquiry to allow partial

[33mcommit 2e16456fa435054790393fe5018c5f632619c824[m
Author: David <devnull@localhost>
Date:   Thu Dec 5 17:10:57 2019 +0700

    TCS_ValidatorFactory register event MOrder BeforeReactivate, AfterReactivate, BeforeVoid

[33mcommit 18a68177771427926564de2281fbef015f0e9a87[m
Author: noel <devnull@localhost>
Date:   Wed Nov 20 11:11:28 2019 +0700

    add description to payment and invoice table on payment allocation

[33mcommit 48cc5da7d712788d31583b61f16cb3e721cdcf78[m
Author: David <devnull@localhost>
Date:   Thu Oct 31 12:17:52 2019 +0700

    MTCSAmortizationLine Change SetHeaderGrandTotal

[33mcommit be95462c8fb0d3731f70cb2cf543dc285ae7c60e[m
Author: David <devnull@localhost>
Date:   Wed Oct 23 12:04:23 2019 +0700

    Add TCS_RfqLineValidator delete MatchRequestRFQ before delete RFQLine

[33mcommit e32457e8a55030dc317b24cb555ad26cf961c7d6[m
Author: David <devnull@localhost>
Date:   Wed Oct 23 12:02:12 2019 +0700

    Recreate Amortization MClass to change account column name with prefix %_ID to %_Acct

[33mcommit 841421302da949b44dbd2fcc6ad3bc34d02ceed7[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Oct 9 07:57:19 2019 +0700

    add entry to hgignore

[33mcommit 6583a72f19262aa461386cf171744f55c9ec496b[m
Merge: 9ae2d5b ad82d7b
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Oct 9 07:56:51 2019 +0700

    Merge

[33mcommit 9ae2d5b90366647ab7e4eccaa570e8b80009bb72[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Oct 9 07:56:42 2019 +0700

    adjust cardinality for validator factory

[33mcommit 34ac067aea4f528a487e28b3d415614b6350063e[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Oct 9 07:53:59 2019 +0700

    edit MANIFEST.MF metadata

[33mcommit ad82d7bc61aad86d4af2abb3e8b4d64c986b3a15[m
Author: David <devnull@localhost>
Date:   Mon Sep 30 22:33:12 2019 +0700

    TCS_Mrequisition ReactivateIt setProcessed to false

[33mcommit eb017a25e6a4a637c1d6812e420366cf11d2fdaf[m
Author: David <devnull@localhost>
Date:   Thu Sep 26 11:16:27 2019 +0700

    TCS_AllocationReset line execute query delete T_MatchAllocation change TrxName from null to same TrxName as delete C_AllocationHdr

[33mcommit 0eeb1cb9317f6170ed28a58b8c59049d5e04f696[m
Author: David <devnull@localhost>
Date:   Mon Sep 16 17:47:16 2019 +0700

    TCS_OrderLineValidator BeforeDelete Add RemoveMatchPR

[33mcommit ece067e52304d4dc98d9a97ef841ec485161cc1d[m
Author: David <devnull@localhost>
Date:   Mon Sep 16 17:46:53 2019 +0700

    TCS_RfqValidator Change used mclass

[33mcommit c5c1a735462a8bec7d81a63f83c187a714c28c6f[m
Author: David <devnull@localhost>
Date:   Mon Sep 16 02:07:38 2019 +0700

    Quotation Create Lines From Inquiry add validation check Quotation Has Match

[33mcommit 6fd5cf1bd492984f79eb2b90c863992be5606cb2[m
Author: David <devnull@localhost>
Date:   Mon Sep 16 00:30:09 2019 +0700

    TCS_QuotationLineValidator before reactivate / void query check match add condition

[33mcommit 2670db2902df05622761788c322193a3ef72ba35[m
Author: noel <devnull@localhost>
Date:   Fri Sep 13 15:36:57 2019 +0700

    adding set isnewproduct for quotationline

[33mcommit 6a49ed573a0c3beb64d6096a55cd6b82639fef1e[m
Author: noel <devnull@localhost>
Date:   Fri Sep 13 14:50:59 2019 +0700

    adding set location commet for asset from asset addition

[33mcommit b662d31e0386a9f00b78f1b9851393fd780fd358[m
Author: David <devnull@localhost>
Date:   Thu Sep 12 17:16:02 2019 +0700

    Change CheckLinkedOrder method

[33mcommit d6380889c125626a103d293caf04e5ca689b5175[m
Author: David <devnull@localhost>
Date:   Thu Sep 12 16:53:40 2019 +0700

    Complete Inquiry Add case DocStatus In Pogress

[33mcommit 3a19c84b0f9f77f230211a78bd40c58b05e6b351[m
Author: David <devnull@localhost>
Date:   Thu Sep 12 16:32:03 2019 +0700

    3045 - Tambahin DocAction Close di Quotation

[33mcommit d7bb624a89658a2a3b7c3db2f69c7a386fb39f11[m
Author: David <devnull@localhost>
Date:   Thu Sep 12 16:31:37 2019 +0700

    3047 - Process Create Outbound Negative Inventory

[33mcommit f66335bcacb0c4f958b0db25d099458272318c40[m
Merge: 04c3d22 f4e822c
Author: David <devnull@localhost>
Date:   Tue Sep 10 22:55:49 2019 +0700

    Merge with Move Match Allocation to TobaERP

[33mcommit 04c3d22d626b553d875552de2c0bfd901aa37cbd[m
Author: David <devnull@localhost>
Date:   Tue Sep 10 14:56:22 2019 +0700

    M_MovementValidator beforeReverse check if isOutbound and has active inbound

[33mcommit 04a6e25aa53e04ba0eff5b37b338219a11fb0757[m
Author: David <devnull@localhost>
Date:   Tue Sep 10 14:55:23 2019 +0700

    TCS_InterWHCreateInbound fix wrong outbound reference getID

[33mcommit 9037cc7613952c26091afa51d6ad7f51a033b7d5[m
Author: David <devnull@localhost>
Date:   Tue Sep 10 14:54:48 2019 +0700

    TCS_ForeignBankRevaluationNoBankStatement Fix query

[33mcommit 3d15ad9a74c48a69939c29d32c5d91166809ea8b[m
Merge: 71be381 c0ce91b
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Sep 10 06:39:21 2019 +0700

    Merge

[33mcommit 71be381211a9be6e449ae857797e8dd5618a15c0[m
Merge: a872181 8194ded
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Sep 10 06:31:37 2019 +0700

    Merge

[33mcommit a872181756a17bbcf7fac96a78e3cf5ee808c990[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Sep 10 06:29:06 2019 +0700

    adding constructor to extended model classes

[33mcommit c0ce91b6614bf495c81e0369324f617556b2bc8e[m
Author: David <devnull@localhost>
Date:   Mon Sep 9 18:06:27 2019 +0700

    2990 - Create multiple Outbound and Inbound

[33mcommit 3c5845a9c6b3d70895f3839b0a23f1d072034601[m
Author: David <devnull@localhost>
Date:   Mon Sep 9 10:59:09 2019 +0700

    3036 - TCS_InterWHCreateOutbound only create line with qty left to outbound

[33mcommit b27f276fa5ed3862c1df4b95046bc8770cef9052[m
Author: David <devnull@localhost>
Date:   Mon Sep 9 10:28:33 2019 +0700

    Add TCS_ForeignBankRevaluationNoBankStatement

[33mcommit b390972ba38dc8c115240b2e58ca71dbdab069e2[m
Author: figonugroho <devnull@localhost>
Date:   Sun Sep 8 19:05:27 2019 +0700

    validate generate outbound

[33mcommit 65d331b35c115be09d5fda26cbb606abe6bdf134[m
Author: David <devnull@localhost>
Date:   Fri Sep 6 12:48:08 2019 +0700

    TCS_Doc_AssetAddition Add case isAdjustAccmDepr='N'

[33mcommit 6b88d611a50aeec7e7eda9a5b25c3e5499a97022[m
Author: figonugroho <devnull@localhost>
Date:   Thu Sep 5 13:07:23 2019 +0700

    callout price when product has price

[33mcommit 15b5399795f0b41ee550a4c943b99390a7679b2a[m
Author: figonugroho <devnull@localhost>
Date:   Thu Sep 5 13:05:38 2019 +0700

    Comment out set price when product has price

[33mcommit 7b2e6164d1ebaf61a7096289f612a237af64184b[m
Merge: 28a1578 5f58d0b
Author: David <devnull@localhost>
Date:   Thu Sep 5 11:15:40 2019 +0700

    Merge with Callout Asset Addition

[33mcommit 28a15785273d810c9134ba2d1c0ec287d686940b[m
Author: David <devnull@localhost>
Date:   Wed Sep 4 21:41:00 2019 +0700

    TCSInquiryReactivate

[33mcommit 03dd7ee7f92ee18e724b510211b7b0c64712368f[m
Author: figonugroho <devnull@localhost>
Date:   Wed Sep 4 21:07:02 2019 +0700

    set price entered to zero

[33mcommit 759b970dc95ccac33568cc77750531508a4302b9[m
Author: figonugroho <devnull@localhost>
Date:   Wed Sep 4 17:30:20 2019 +0700

    Set Price QuotationLine

[33mcommit dbae71a4156ace9a948af14c63398f72ba9eba61[m
Author: David <devnull@localhost>
Date:   Wed Sep 4 14:34:51 2019 +0700

    MovementValidator fix get wrong column name

[33mcommit c6fa20e0a678f212bad6d7d4ae3525f078278dac[m
Author: David <devnull@localhost>
Date:   Wed Sep 4 14:34:08 2019 +0700

    Quotation Line Generated From Inquiry SET C_QuotationLine.C_InquiryLine_ID

[33mcommit 5f58d0b4e2bceca644946e67e6d012d7437e8cff[m
Author: noel <devnull@localhost>
Date:   Wed Sep 4 13:28:55 2019 +0700

    changes validation

[33mcommit deb1a8dead9f18f50cea94fe8bf138b4c8416908[m
Author: David <devnull@localhost>
Date:   Fri Aug 30 01:06:58 2019 +0700

    Manifest Export Package id.tcs.model

[33mcommit 2e829b0c04b5af7099d71b493ba8e458de66aab9[m
Author: noel <devnull@localhost>
Date:   Thu Aug 29 13:18:34 2019 +0700

    change getpriceenterd from responseline for isincludedrfq to pricelist

[33mcommit c0d2832a8bbfa2df528975e67b08624816a146c3[m
Author: David <devnull@localhost>
Date:   Wed Aug 28 19:08:09 2019 +0700

    comment Callout Inquiry & Quotation set size

[33mcommit 70dad64756839fe5a5e708be4fbe05d41ecb1912[m
Author: figonugroho <devnull@localhost>
Date:   Thu Aug 22 14:35:36 2019 +0700

    revision process tax

[33mcommit abcbdf0e45748c32bfe848cec4ac195b585d2301[m
Author: David <devnull@localhost>
Date:   Thu Aug 22 11:07:30 2019 +0700

    TCS_InterWHCreateOutbound comment line process complete to allow partial outbound

[33mcommit d7decabfee794f474b93d61135f93cd651918422[m
Author: David <devnull@localhost>
Date:   Thu Aug 22 11:06:01 2019 +0700

    Add pop-up window after process to jump to new record

[33mcommit 92445a8757f83c866a02f8aba273a338efd65ceb[m
Author: David <devnull@localhost>
Date:   Thu Aug 22 11:04:28 2019 +0700

    2990 - Create multiple Outbound and Inbound, (Before void DD_Order validate no active in/out bound)

[33mcommit 94e26032b8cbea93504fadd3e572f8e5bbbb0521[m
Author: noel <devnull@localhost>
Date:   Thu Aug 22 10:00:33 2019 +0700

    commit callout asset addition

[33mcommit 9744a796887bebe5ac2ad73c94269c87e1f76393[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 16:39:50 2019 +0700

    TCS_InterWHCreateInbound change inbound to depentent to outbound

[33mcommit 2c0b1e471fcf158fb8a84fdaf4802396ac7f9313[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 16:38:32 2019 +0700

    TCSInquiryToRFQ add pop-up window after process

[33mcommit 2a1dbe7679438cd62878fcf92e4b9c00c5c18f18[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 16:38:00 2019 +0700

    TCS_DDOrderValidator add validate in/out bound qty =< DD_OrderLine.qty

[33mcommit 45b3cfa6b856eee6b203718aca2f95f029b6b1aa[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 16:36:37 2019 +0700

    Create TCS_MDDOrder for DocOptions

[33mcommit 170900dd145f7a89c0ad9a2fc26155a91404a744[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 11:15:52 2019 +0700

    TCS_MRequisition override ReactiveIt(), comment line calling reverseCorrectIt()

[33mcommit 54ececf16900a186ef656e0b12ff997cc3466ed6[m
Author: David <devnull@localhost>
Date:   Wed Aug 21 11:15:05 2019 +0700

    Process Complete all Drafted AllocationHdr of running client

[33mcommit f4e822ca8760732d056a2ebef40742870fed86c4[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 22:07:42 2019 +0700

    Add after complete save allocation when C_Charge_ID not null

[33mcommit 9bd2f5f9d58b2eb92569b3b36912444b7e4351ff[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 22:07:05 2019 +0700

    Comment AllocationHdr validator from validataor factory

[33mcommit 39686f83af5b9ef4f791f2051b8ba03d7760910e[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 15:29:03 2019 +0700

    2990 - Create multiple Outbound and Inbound

[33mcommit 32c1c50ef55d238b5a9ea1cf51d061a7752e7f4b[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 14:22:14 2019 +0700

    Move Match Allocation Related class to core TobaERP

[33mcommit c804459dad73ca5b924e46436687e6fc424efaa0[m
Author: figonugroho <devnull@localhost>
Date:   Tue Aug 20 11:18:10 2019 +0700

    Process for change tax on invoiceline

[33mcommit 69c14361660cea6f494d57e69103a1b1f83a4758[m
Author: figonugroho <devnull@localhost>
Date:   Tue Aug 20 11:17:29 2019 +0700

    Change Parameter for RFQ

[33mcommit 11297818af5b505c57bd604b8918d0ba322d4805[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 00:53:13 2019 +0700

    Inbound & OutBound

[33mcommit 758c2344d1cd1c3e7266ada0a6b5ec65163d5538[m
Author: David <devnull@localhost>
Date:   Tue Aug 20 00:52:55 2019 +0700

    TCS_COrderCreateDDOrder

[33mcommit c22fcdcc0230427b6fb632470cb1269ba964ec9c[m
Author: David <devnull@localhost>
Date:   Mon Aug 19 22:47:21 2019 +0700

    2991 - Create Outbound Error (Change method of getting WH & Locator In-Transit)

[33mcommit 5c9bd958bbdfd32891d86c012a3cd8552cfe18c4[m
Author: David <devnull@localhost>
Date:   Mon Aug 19 18:34:51 2019 +0700

    2989 - revert some unnecesary part

[33mcommit 8194ded7ccc27dfc784a1a08e303bf49c70ff20e[m
Author: David <devnull@localhost>
Date:   Mon Aug 19 17:54:55 2019 +0700

    2991 - Create Outbound Error (Comment lines related to ad_inventoryorg, change get in_transit WH from parameter)

[33mcommit 8849a027eed5fbb01c789686b0224167880a1eb8[m
Author: David <devnull@localhost>
Date:   Mon Aug 19 17:42:25 2019 +0700

    2989 - Create Inter Warehouse from SO

[33mcommit 6b3f3bd3109aed897a5443c3db2a19e474d8c7b5[m
Author: figonugroho <devnull@localhost>
Date:   Thu Aug 15 18:11:40 2019 +0700

    Add validation for create order

[33mcommit 14092c1d8b1c96e721dd3d431e5226eb31e7fd74[m
Author: figonugroho <devnull@localhost>
Date:   Thu Aug 15 18:09:55 2019 +0700

    Add parameter inquiry

[33mcommit 7f20f0b38988e9e24ddd9d2b828f8d9a5596cafb[m
Author: figonugroho <devnull@localhost>
Date:   Wed Aug 14 13:40:31 2019 +0700

    Validate QuotationLine

[33mcommit ca95394f0cc56c3a64b0ed506e11046a4a3a4de5[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 18:09:13 2019 +0700

    Change set value M_PriceList direct from bpartner on inquiry

[33mcommit 5921eec1a39d2f5226cef7852f45a2d0ad0cf7df[m
Author: David <devnull@localhost>
Date:   Mon Aug 12 10:27:23 2019 +0700

    TCS_CreateMCostForProductWithImcompleteCostElement

[33mcommit 3aaaee30e8c36029b5384fa67d25028bfa131bfc[m
Merge: e44ae40 b9aeb88
Author: David <devnull@localhost>
Date:   Mon Aug 12 10:26:18 2019 +0700

    Merge

[33mcommit e44ae4072b3cfe203af6e927286ef977889a2ec4[m
Author: David <devnull@localhost>
Date:   Mon Aug 12 10:25:51 2019 +0700

    Add TCS_QuotationCreateLinesFromInquiry

[33mcommit 7190c034fc928039bc7586052777b73a7f400bc1[m
Author: David <devnull@localhost>
Date:   Mon Aug 12 10:25:29 2019 +0700

    Remove duplicate TCS_MatchAllocation MClass

[33mcommit b9aeb88cd79b151d1e5d978d811605ceb90a211e[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:20:15 2019 +0700

    Add trigger for reActivateIt

[33mcommit 3385fcd863ba08cf19deddc601fea47161c79a6d[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:16:59 2019 +0700

    Add factory for RFQ

[33mcommit 4cc7b5edb34166dd289f722df5bbf90bf912f0d0[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:14:05 2019 +0700

    Commend out get value from response line

[33mcommit 69694eb2cff7fbdc729641a2a1d1c55ad55e88b1[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:12:40 2019 +0700

    Add callout for quotation

[33mcommit 5ea65b6797e53c40795cebe4a37a4ed56138873e[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:11:27 2019 +0700

    Commend validation for create order without winning and accepted quotation

[33mcommit 2677de1a067dfbb0b61b4af991802630a0a592fd[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:08:55 2019 +0700

    Add validator for delete match quotation when delete order line

[33mcommit 4768ab6eec2c6b2a369c89e500c8f99f018c93f0[m
Author: figonugroho <devnull@localhost>
Date:   Mon Aug 12 10:06:19 2019 +0700

    Add validator for check reactivate quotation has linked with order

[33mcommit 13f20bc2111dd84167c75ec60bd442aa33371b07[m
Author: David <devnull@localhost>
Date:   Fri Aug 9 14:08:08 2019 +0700

    TCS_MAllocationHdrValidator move create match allocation from MClass to Validator

[33mcommit e0a162f288fe49fb66cc70b66cf83e799872ea5e[m
Author: David <devnull@localhost>
Date:   Fri Aug 9 14:07:15 2019 +0700

    MClass TCS_MInvoice DocOption change transition "DR > IP" to "DR > CO"

[33mcommit 4c1d14fb78fcc5d63c8041ac79c1e37c839721b8[m
Author: David <devnull@localhost>
Date:   Fri Aug 9 14:06:19 2019 +0700

    Update manual create match allocation process method

[33mcommit 48812794d92843e40547920a13136f9163ff3d70[m
Author: figonugroho <devnull@localhost>
Date:   Tue Aug 6 13:22:43 2019 +0700

    change process inquiry to quotation and move to another package

[33mcommit 42bfe5ba8d5a3412d997ec77700a5d3a50e112dd[m
Author: David <devnull@localhost>
Date:   Tue Aug 6 11:35:50 2019 +0700

    TCS_PaymentValidator Fix Typo

[33mcommit 45ea491d60998ad41da0a3b6416685e2bde753d7[m
Merge: a8f2c1e 0e70d90
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Aug 5 14:21:05 2019 +0700

    Merge

[33mcommit a8f2c1eb8fda5c30ff9de5b4e5f01893197ae0f3[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Aug 5 14:19:13 2019 +0700

    add process update credit status BP

[33mcommit 0e70d90a6d635475f7c92455b5d12059526f4720[m
Author: David <devnull@localhost>
Date:   Mon Aug 5 13:47:17 2019 +0700

    TCSBankRegister fix Typo

[33mcommit 23c37c0dfaa671f91b266756ab0330c42bb2279a[m
Author: David <devnull@localhost>
Date:   Mon Aug 5 13:40:42 2019 +0700

    Move CreateMatchAllocation from MClass to Model Validator

[33mcommit 06378f12d323d5cfbe30023d87b1565936ab2cf2[m
Author: David <devnull@localhost>
Date:   Mon Aug 5 13:39:46 2019 +0700

    TCSBankRegister add bankaccount name to totle row

[33mcommit f1fc4747c79a461c0d6d9bd7253fe57d5bbd8580[m
Author: David <devnull@localhost>
Date:   Mon Aug 5 13:38:56 2019 +0700

    InterWH Doc_DDOrder

[33mcommit 47906b57ee978577f855765157f2474d2da035af[m
Author: noel <devnull@localhost>
Date:   Fri Aug 2 17:54:19 2019 +0700

    commit add model to modelfactory

[33mcommit 6ae6ccc6b34de689c89622d3deb95bdf83a994e0[m
Author: noel <devnull@localhost>
Date:   Fri Aug 2 14:29:28 2019 +0700

    commit changes on I_TCS_AmortizationRun (Table_ID) from 300312 to 300532

[33mcommit 6ece261a4bead97c1ff1d37406121476f7dee4ff[m
Author: David <devnull@localhost>
Date:   Fri Aug 2 13:56:45 2019 +0700

    Payment Validator CheckAllocation Exclude Case C_Payment.C_Charge_ID IS NOT NULL

[33mcommit f0335e7a57b87b2e0f3f4f0b0db67ed7f5acf615[m
Author: noel <devnull@localhost>
Date:   Fri Aug 2 09:53:55 2019 +0700

    commit changes on I_TCS_AmortizationLine (Table_ID) from 300311 to 300531

[33mcommit 5162f75b1066136fa8e3908154b8232f1c7c37c2[m
Author: David <devnull@localhost>
Date:   Thu Aug 1 14:30:03 2019 +0700

    TCS_CalloutDDOrderLine

[33mcommit 48d822052aa51a5f05aed2f51c12754cf9b7f6ca[m
Author: noel <devnull@localhost>
Date:   Thu Aug 1 13:25:57 2019 +0700

    commit change modelfactory service.ranking 99 to 1000

[33mcommit fc310bf76d017e1b2b49a6b1c9178a4e0378479f[m
Author: noel <devnull@localhost>
Date:   Thu Aug 1 11:49:50 2019 +0700

    commit add MTCSAmortizationPlan

[33mcommit f88e23fe7c8d34db0953cf1a64dfeacfbf671a57[m
Author: noel <devnull@localhost>
Date:   Thu Aug 1 11:41:30 2019 +0700

    commit changes on I_TCS_AmortizationPlan (Table_ID)

[33mcommit b732eb98901519757d8f37bec1f498a1446251c8[m
Author: David <devnull@localhost>
Date:   Mon Jul 29 16:29:31 2019 +0700

    TCS_CopyClientRoles

[33mcommit 82e32da564f22d7e938028b46d0e0508827fc51c[m
Author: David <devnull@localhost>
Date:   Mon Jul 29 16:29:08 2019 +0700

    TCS_CreateMCostDetail

[33mcommit 5b74b20ee225571f374de68cdb1be320dfeb6d3b[m
Author: David <devnull@localhost>
Date:   Mon Jul 29 16:28:45 2019 +0700

    TCS_InterWH

[33mcommit 278b8d914a4dd40cbf25f6b04b8a1ce5776f2c07[m
Author: David <devnull@localhost>
Date:   Mon Jul 29 16:28:32 2019 +0700

    Warehouse Access

[33mcommit b823ec05024beca254fd090e44e2f2c82d00494a[m
Author: David <devnull@localhost>
Date:   Mon Jul 29 16:27:31 2019 +0700

    TCS_TaoStockCard

[33mcommit a3e305c257871f1c5155a02c7de13deea5982b7c[m
Author: David <devnull@localhost>
Date:   Tue Jul 23 16:25:22 2019 +0700

    Amortization add process

[33mcommit 938e5fd55d0a319260cd7be86b46f7b76dfeb628[m
Author: David <devnull@localhost>
Date:   Tue Jul 23 13:48:18 2019 +0700

    Amortization

[33mcommit 8d87ba9cde1df3e70ae760f46d476e6b81a73aa7[m
Author: noel <devnull@localhost>
Date:   Fri Jul 19 21:21:05 2019 +0700

    Bank transfer (New) remove allocation line for charge transfer

[33mcommit 701f67bb358374c3ddfaa10ed4ec2d46baeca78c[m
Author: noel <devnull@localhost>
Date:   Fri Jul 19 19:59:22 2019 +0700

    Bank Transfer (New) added validation for idr fee

[33mcommit 078aa6565bab1cc3a8af8520a340157036cc562c[m
Author: noel <devnull@localhost>
Date:   Fri Jul 19 17:58:59 2019 +0700

    Bank transfer (new)

[33mcommit a6a709dc2ac1c06051a60ad8c3fd150abf569c71[m
Author: David <devnull@localhost>
Date:   Fri Jul 19 10:38:06 2019 +0700

    Rename custom Doc_AssetAddition class to TCS_Doc_AssetAddition

[33mcommit 885e7c5cce532eb0616e1708f23d544c1542d6b7[m
Merge: 523bdba cff4d8d
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 18 23:28:22 2019 +0700

    Merge

[33mcommit 523bdbae3991b2113b122c921dcc46d04f7d1941[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 18 23:27:45 2019 +0700

    temporarily add Doc_AssetAddition

[33mcommit cff4d8d938610129977ea009db16f93cb1f5aad0[m
Author: noel <devnull@localhost>
Date:   Tue Jul 16 16:32:06 2019 +0700

    Bank Transfer Add validation for different currency transfer (both are not IDR)

[33mcommit 2f024531dfb8df412ffc403aafaf12b1bee9e65c[m
Author: David <devnull@localhost>
Date:   Tue Jul 16 15:01:56 2019 +0700

    TCS_MPayment temporary override beforeSave, comment SetIsPrepayment part

[33mcommit 3c08324b84019bde579615fdb280fe9fcf0ab13b[m
Author: David <devnull@localhost>
Date:   Tue Jul 16 15:01:05 2019 +0700

    TCS_MInOut DocOption Add Drafted > Complete

[33mcommit 80ac67ec396cd6521861e796a0fba24ced78ef66[m
Author: David <devnull@localhost>
Date:   Tue Jul 16 15:00:40 2019 +0700

    TCS Validator change return massage

[33mcommit 7046c38be6d7d12a0b355a805fe7dc3eab16a796[m
Author: noel <devnull@localhost>
Date:   Tue Jul 16 10:26:41 2019 +0700

    commit bank transfer(new)

[33mcommit 5b229853b97e91b6ed46faadd72b9df494d8287c[m
Author: David <devnull@localhost>
Date:   Fri Jul 12 15:06:57 2019 +0700

    TCS_CreateFromOrder

[33mcommit ef527e40dac4a42f9a323297f1c3d4ed0c5d11b7[m
Author: David <devnull@localhost>
Date:   Fri Jul 12 15:06:29 2019 +0700

    TOBA-028 fix typo

[33mcommit ebbcfaa732dff29de6ba22f9d6b32aeb5121d18a[m
Author: David <devnull@localhost>
Date:   Fri Jul 12 15:05:42 2019 +0700

    Quotation

[33mcommit 6be504c5b09e3aca346ed4cb4aa4e52ec82a7280[m
Author: David <devnull@localhost>
Date:   Fri Jul 12 15:05:05 2019 +0700

    Quotation

[33mcommit b67ceff68f83682946cd3736d9bbb860275e13a2[m
Author: noel <devnull@localhost>
Date:   Thu Jul 11 15:18:23 2019 +0700

    Commit Bank Transfer (New)

[33mcommit 8cecfcc548af993c4c9a41768520fa8eac38e124[m
Author: David <devnull@localhost>
Date:   Wed Jul 10 14:34:19 2019 +0700

    TCS_createFromOrder

[33mcommit 2740e396951bec353a364cb8840afd82040cf4b4[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Jul 9 15:17:10 2019 +0700

    refactor MBankTransfer

[33mcommit ffeb51cb35cfe2acd23c72ec2c46879c3b9bcf15[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Jul 9 15:11:46 2019 +0700

    refactor MBankTransfer

[33mcommit 07601bb6d7e795b9576d730cb9fbb6f6bc0c4f11[m
Merge: e1e85cf 1b10f69
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Jul 9 14:53:16 2019 +0700

    Merge

[33mcommit e1e85cfcb54bf59b035534e85db13a7ecd156a55[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Tue Jul 9 14:53:08 2019 +0700

    refactor MBankTransfer

[33mcommit 1b10f697a54775005a35b8507f0036d13f137731[m
Author: David <devnull@localhost>
Date:   Mon Jul 8 14:15:21 2019 +0700

    TCS_AllocationReset Fix Type

[33mcommit 145d48d77940bd214467b5bc8630a90144fdf17b[m
Author: figonugroho <devnull@localhost>
Date:   Fri Jul 5 16:21:11 2019 +0700

    callout simplify (change C_ElementValue_ID)

[33mcommit 87160d0cb6fafbd67aaa48f416383229827f6851[m
Author: noel <devnull@localhost>
Date:   Thu Jul 4 16:37:26 2019 +0700

    commit callout simplify (new) @leo

[33mcommit 472ed071e748713e88e4cbc93a4fd5096a9ee077[m
Merge: 0d4d57e c057fb7
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 16:12:44 2019 +0700

    Merge

[33mcommit 0d4d57eb1e69820159c61b8f6ebae7d06a9368ba[m
Author: noel <devnull@localhost>
Date:   Thu Jul 4 16:06:48 2019 +0700

    commit callout simplify(new) @figo

[33mcommit 34c0f04e31a1c1af6a668e174d2fcec9a05dcf47[m
Author: noel <devnull@localhost>
Date:   Thu Jul 4 15:47:24 2019 +0700

    commit calloutfadefaultaccount

[33mcommit c057fb74350139ddd4ec0f9a6dddcaa2883e6483[m
Merge: dc6de91 cf2aa7e
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 15:18:54 2019 +0700

    Merge

[33mcommit dc6de91b41fa5739b6261f5b581eab14dc4c72f7[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 15:15:21 2019 +0700

    TOBA-038 Cannot complete receipt / shipment that has line not associated to existing order line

[33mcommit cf2aa7ed702aceb90b0d57063bd134a517b7aff2[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 15:11:21 2019 +0700

    TOBA-037 Cannot reverse / void shipment that has been converted to invoice

[33mcommit 4c022d7b1e3cb0428c3ee23529e173839fb17cae[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 14:49:34 2019 +0700

    TOBA-036 Cannot reverse / void / reactivate SO that has been converted to shipment

[33mcommit fb5253509649abc4310999e31432eaf8026a152b[m
Merge: 38c1335 71841eb
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 14:37:11 2019 +0700

    Merge

[33mcommit 38c1335f65eb0aabe5bd8b4a95fbadf0f4b47722[m
Merge: eaf17c5 063e4e9
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 14:31:50 2019 +0700

    Merge

[33mcommit eaf17c57fe7521094c739528260788fe55c142b9[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 14:27:55 2019 +0700

    fix callout Simplify account

[33mcommit 063e4e95d471f37a61a037970f17243ea47cd14e[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 14:25:05 2019 +0700

    TOBA-029 Cannot Reverse InOut That Has Match Invoice Record With Invoice.DocStatus IN ('CO','CL','IP')

[33mcommit 1be04dbd30b51cbb99b563f89ed26f35d660034f[m
Merge: dc65283 94d6adb
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Thu Jul 4 13:24:53 2019 +0700

    Merge with Commit_MClass_BankTransfer

[33mcommit dc652837d9dfb82ada511c86cc010ba0288d8e79[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 12:07:49 2019 +0700

    TOBA-035 Cannot Reactive Order when linked to Payment

[33mcommit 71b2b24db4e5c71223094200ca7a9fe6cc962358[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 12:00:34 2019 +0700

    TOBA-034 Allocation Line created from TCS_AllocateCharge GET C_BPartner_ID FROM TCS_AllocateCharge

[33mcommit 30c38943b39f05fa8959ab5e63f4035efcc95f96[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 11:23:02 2019 +0700

    Fix Alloc Line get Description AND Create Allocation Line for C_PaymentAllocate AND TCS_AllocationCharge in one AllocationHdr

[33mcommit b296b2109194e12c583f4d35991e4b8d3d03cc04[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 11:15:10 2019 +0700

    TOBA-030 Set Payment.PayAmt=SUM of C_PaymentAllocate AND TCS_AllocateCharge Amount

[33mcommit 9c4eff3c98f1627d80fc8d03a8ae0a72401a9deb[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 10:14:34 2019 +0700

    TOBA-033 Allocation Line Created From C_PaymentAllocate AND TCS_AllocateCharge Get Description From Source

[33mcommit 2ea4435ae824ed9ebcf2f7c113fcce0545c2c5f0[m
Author: David <devnull@localhost>
Date:   Thu Jul 4 10:07:21 2019 +0700

    TOBA-032 Complete Payment Process Both C_PaymentAllcoate And TCS_AllocateCharge

[33mcommit 6d59fdfaae2ba415a4f30a0e387a004f3745941d[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 17:09:49 2019 +0700

    Fix TCS_MPayment change package name

[33mcommit b9be392ceb36240ffadb90b4c112016f035da8b4[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 17:09:11 2019 +0700

    TOBA-030 Set Payment.PayAmt = SUM of C_PaymentAllocate & TCS_AllocateCharge Amount

[33mcommit 90a21e236511456f3f1de0244e587555e0f310fe[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 17:07:27 2019 +0700

    TOBA-026 Cannot Reset Allocation for Payment allocate to multiple Charge OR Payment using "Allocate" tab

[33mcommit f17603a0dceb244a6657e24c010e7533512d0c44[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 17:06:07 2019 +0700

    TOBA-031 Override MPayment.PrepareIt() To Include TCS_AllocateCharge For verifyPaymentAllocateSum() Function

[33mcommit d6326b42343f714affbd7d7f4b40903fdfafc0a7[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 17:04:21 2019 +0700

    TOBA-028 Cannot reverse / void / reactivate PO that has match PO record

[33mcommit 8273b08e32e0e7f77cd1e3685a0c70a47febc140[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 14:53:35 2019 +0700

    TCS_MPayment fix typo

[33mcommit 1b2359972850fa1353936782ef0cf49d6e011f08[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 11:50:00 2019 +0700

    TOBA-028 Cannot reverse / void / reactivate PO that has match PO record

[33mcommit f68cb237e214f87c2a4a399c830501c3d00ced05[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 11:46:07 2019 +0700

    TOBA-027 Cannot reverse / void PR that has link to PO

[33mcommit 94c554b80b6ac8cf236ebef871ffe299447c903c[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 11:44:57 2019 +0700

    TOBA-021 Cannot reverse / void Payment that has allocation records (except Payment allocated with "Allocate" & "Allocate Charge" Tab)

[33mcommit 42309d7dbdc7f172f6d3b30be4722363b2192351[m
Author: David <devnull@localhost>
Date:   Tue Jul 2 11:43:48 2019 +0700

    TOBA-022 Cannot reverse / void Invoice that has allocation records

[33mcommit 71841ebc9c253fd653936a5bc0fee46e186f4811[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 19:21:08 2019 +0700

    TOBA-025 Refactor unnecessary overriding code in TCS_MPayment

[33mcommit 54f21943614a34716b171e4b24f8ad206ba0bb7a[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 19:02:50 2019 +0700

    TOBA-025 Add extension M-class for all document M-classes

[33mcommit 6021f9bdff527a527fb3faaa56df61643f8572b4[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 18:57:00 2019 +0700

    TOBA-025 Add extension M-class for all document M-classes

[33mcommit 8a58b8c5f782f2285613cc66dc954a39c120d2cb[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 18:55:00 2019 +0700

    TOBA-025 Add extension M-class for all document M-classes

[33mcommit cceacf046a6c658df3e7f15958013650dcbb92bd[m
Author: David <devnull@localhost>
Date:   Mon Jul 1 14:29:37 2019 +0700

    TOBA-021 Cannot reverse / void Payment that has allocation records

[33mcommit d6ac9f8d048244822f67b060dcd1b7fb3a820b23[m
Author: David <devnull@localhost>
Date:   Mon Jul 1 13:32:30 2019 +0700

    Update Model & Validator Factory

[33mcommit 72d7430fa55db2ff3998938dd6915d1e72a07f21[m
Merge: 2f0cb53 981933c
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 12:47:05 2019 +0700

    Merge

[33mcommit 2f0cb53f09559f0a0f4092f2a853476d666df30b[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Mon Jul 1 12:43:15 2019 +0700

    Add validation in invoice to check related order and inout before complete; and to check related allocation before reverse

[33mcommit 981933c28034eddea4f4dbf799952e87acc7a967[m
Author: David <devnull@localhost>
Date:   Mon Jul 1 12:25:54 2019 +0700

    Move tcs-one specific class from SLU repository

[33mcommit 04b4d80a94987360d9aa63379550ca053856533c[m
Author: David <devnull@localhost>
Date:   Thu Jun 27 18:07:00 2019 +0700

    TCS_CreateMatchAllocationByCreatedDate

[33mcommit c20f69967bda85531a148ec2b06d1bc8f403412e[m
Author: David <devnull@localhost>
Date:   Thu Jun 27 18:06:45 2019 +0700

    TCSBankRegisterNoRecon

[33mcommit 94d6adb608a02e760aa9e5884608b2cc2714908a[m
Author: noel <devnull@localhost>
Date:   Thu Jun 27 15:20:22 2019 +0700

    Commit M_Class BankTransfer(New)

[33mcommit 4bcd295a3cdda6357971ee50faf9c9bbb3f8c6e8[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 21:32:36 2019 +0700

    standardize column for tcs general ledger view

[33mcommit 50b2fc906309abaec43f7f500dcdc10289ff026f[m
Author: noel <devnull@localhost>
Date:   Wed Jun 26 19:45:43 2019 +0700

    Commit M Clas Bank Transfer (Revisi)

[33mcommit f06655488274003f07cc99223e41df8c110a887c[m
Author: noel <devnull@localhost>
Date:   Wed Jun 26 18:47:07 2019 +0700

    Commit M Class Bank Transfer (New)

[33mcommit 3e0f8093ad5149ece3a1b8f1d15b55de0692e3d2[m
Author: David <devnull@localhost>
Date:   Wed Jun 26 18:18:06 2019 +0700

    TCSGeneralLedgerView comment column C_Acct_Schema_Name

[33mcommit 806b103801ab99998a5a6a1046e221ce01e6e392[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 18:12:40 2019 +0700

    remove project specific custom code

[33mcommit 1f669d7445be92533d78cc68eb69406e41276232[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 18:12:18 2019 +0700

    remove project specific custom column from TCSAllocation

[33mcommit 5293ac941ba72b2c80465daeeba2e8f77ad08381[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 14:06:31 2019 +0700

    standardize column for tcs general ledger view

[33mcommit 6e6380e277903ad7dcc400b4166ce72240ed8da9[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 14:06:16 2019 +0700

    standardize column for tcs bank register

[33mcommit 9d1e58a93bb2a57377355f71991593fc5073097d[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 14:05:50 2019 +0700

    add custom payment allocation with field for description

[33mcommit 97103d078cbc4e8513ae399d0a97e0d75f683578[m
Author: edwin@tobaconsulting.com <edwin@tobaconsulting.com>
Date:   Wed Jun 26 14:05:25 2019 +0700

    recreate model class for tcs allocate charge

[33mcommit e2f74e3f9c0b12dc5be90e946baea36020bd5ad7[m
Author: noel <devnull@localhost>
Date:   Tue Jun 25 18:18:57 2019 +0700

    Commit M Class Bank Transfer

[33mcommit d92c723021453d0632b3169b1db247c7b043a75b[m
Author: figonugroho <devnull@localhost>
Date:   Wed Jun 19 17:05:10 2019 +0700

    Change C_ElementValue_ID to C_ElementValue_Charge_ID on CalloutChargeAcct

[33mcommit 4c097c95fad9a5dd359e34afd311444b981cdf33[m
Author: noel <devnull@localhost>
Date:   Wed Jun 19 13:29:12 2019 +0700

    commit simplify callout code and adding IAccountGen

[33mcommit 1d9d1774a4a7369188f7d53dd6c2157eedb951fd[m
Author: figonugroho <devnull@localhost>
Date:   Tue Jun 18 17:48:13 2019 +0700

    Commit Callout Simplify Account

[33mcommit bacea01b1f0e3f239f1fdc44408d046aadc9390d[m
Author: David <devnull@localhost>
Date:   Tue May 28 16:29:09 2019 +0700

    TCSBankRegister CreateReconcoledLines change join query to C_Payment and C_Bpartner to Left Join

[33mcommit 017de02a623881325e3e33e1b0df3a991dbc78e8[m
Author: David <devnull@localhost>
Date:   Tue May 28 16:28:04 2019 +0700

    Withholding

[33mcommit b788b67f472029097ef90532c7e4d4a0db527040[m
Author: David <devnull@localhost>
Date:   Thu May 23 14:01:37 2019 +0700

    Calcuate Withholding

[33mcommit 95062eff632eb4c3a16cb12515891d626a508d2b[m
Author: David <devnull@localhost>
Date:   Thu May 23 14:00:47 2019 +0700

    Callout Asset Reval

[33mcommit cffe6550752eb0e1dd444926d5e2491a34cd1fd2[m
Author: David <devnull@localhost>
Date:   Thu May 23 13:59:55 2019 +0700

    Validator Factory Add service.ranking value 100

[33mcommit a34ff04dcb94b4d6c5d64ef968c8c777b99b2078[m
Author: David <devnull@localhost>
Date:   Fri Mar 22 11:06:19 2019 +0700

    Callout Asset

[33mcommit b75d35e9e09ff91c096c9ab34f2e33e41ed4679c[m
Author: David <devnull@localhost>
Date:   Thu Mar 21 13:22:18 2019 +0700

    Add Withholding

[33mcommit 51a557cf6dd74b1916fd104964c5f1f101becb9d[m
Author: David <devnull@localhost>
Date:   Thu Mar 21 13:20:51 2019 +0700

    Process CreateLeftoverMatchAllocation for allocationHdr not exist in T_MatchAllocation

[33mcommit e15176e3c210e0595a05af4095fb4fe57d8d6cc1[m
Author: David <devnull@localhost>
Date:   Thu Mar 21 13:19:44 2019 +0700

    Process General Ledger Report

[33mcommit f5609e9e8e34318c4d5a7c47626968a8fde7a253[m
Author: David <devnull@localhost>
Date:   Fri Feb 15 15:53:01 2019 +0700

    Add Withholding

[33mcommit cd0a70f3966c812e0b7b130acd5a7b2c4ade02c0[m
Author: David <devnull@localhost>
Date:   Fri Feb 15 15:52:33 2019 +0700

    Add Process Create Match Allocation with parameter AllocationHdr

[33mcommit e70b1f8ba7ec2491616da53872b221ca8ee20049[m
Author: David <devnull@localhost>
Date:   Mon Jan 7 14:47:17 2019 +0700

    Change Match Allocation Description Case Payment - Payment

[33mcommit dd689e9327bad4021da1e4949916e080ba3439a7[m
Author: David <devnull@localhost>
Date:   Mon Jan 7 14:46:33 2019 +0700

    TCS_ForeignBankRevaluation Fix Diff Rounding

[33mcommit ac018291d240d9904f25d1eec4b99bbc9da9bed9[m
Author: David <devnull@localhost>
Date:   Mon Jan 7 14:44:56 2019 +0700

    TCS_MInvoice Process Travel Expense

[33mcommit dbe63a6a89b453fdffa2347d6f93f27c1249324f[m
Author: David <devnull@localhost>
Date:   Mon Jan 7 14:43:58 2019 +0700

    Update Travel Expense MClass

[33mcommit c01aeef98d237e78a444e3d49e2f5dd368b46a2e[m
Author: David <devnull@localhost>
Date:   Thu Jan 3 15:26:29 2019 +0700

    Process Create Match Allocation, Add Case Get Match Allocation Description From TCS_Allocate Charge

[33mcommit e8ab66e1abccfe417d978ffd8ea1045f9cee7122[m
Author: David <devnull@localhost>
Date:   Fri Dec 28 16:19:49 2018 +0700

    Implement Bank Revaluation Report & Class

[33mcommit 04053f0da089a22305cec0a500a430b52f0426cd[m
Author: David <devnull@localhost>
Date:   Fri Dec 28 16:18:59 2018 +0700

    Create Match Allocation, Add Case Get Match Allocation Description From TCS_Allocate Charge

[33mcommit e3226f2354e63fa066036eb1295d9b905200e99a[m
Author: David <devnull@localhost>
Date:   Thu Dec 20 17:52:28 2018 +0700

    T_MatchAllocation Set Description By Case

[33mcommit b793208b49e6990800c28b17ca7cd0a6b559c3f1[m
Author: David <devnull@localhost>
Date:   Mon Dec 17 11:17:58 2018 +0700

    Update MClass Adv Request & Settlement

[33mcommit 1046d1e74294bd1afe2329b2cf5d698e55dc5954[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Dec 14 11:59:33 2018 +0700

    added description for t_matchallocation in process and model

[33mcommit 225e413da0e7cf776f3929a0450574769463f257[m
Author: David <devnull@localhost>
Date:   Tue Dec 11 11:31:08 2018 +0700

    Advance Request & Settlement Create Invoice & Payment, Add Description

[33mcommit bc7a99c537083ae8308569d94c7129b9409d55b0[m
Author: David <devnull@localhost>
Date:   Tue Dec 11 11:30:16 2018 +0700

    Update Destination Request & Settlement I&X Class

[33mcommit 24a2302e1f6c36e921ea8bcdf330ebe328cf2c35[m
Author: David <devnull@localhost>
Date:   Tue Dec 11 11:29:40 2018 +0700

    Update Callout DestSettlement.IsReturenTrip

[33mcommit 488422305121c5aeb52e4331cad5742d19c6ffae[m
Author: David <devnull@localhost>
Date:   Thu Dec 6 11:32:18 2018 +0700

    TCS_M_Payment Comment Set IsPrepayment to False Before Save

[33mcommit d00a772f60a60726df828befa624f13bf770d9e3[m
Author: David <devnull@localhost>
Date:   Thu Dec 6 11:32:14 2018 +0700

    TCS_Doc_Payment When C_Charge_ID !=0  Set Fact_Acct Charge Account To Account From Bank

[33mcommit d7a4550281f6f2298740d44f50a1703e571d9c18[m
Author: David <devnull@localhost>
Date:   Thu Dec 6 11:27:53 2018 +0700

    Update Advance Request & Settlement

[33mcommit 488d5234bd4eda13e2855c6ba1ba33f0dc5fd2a4[m
Merge: a1f0d2b 5b5d085
Author: David <devnull@localhost>
Date:   Wed Dec 5 10:15:24 2018 +0700

    Merge

[33mcommit a1f0d2b55470829f6b6b2068c001a2e2dfa9d5f2[m
Author: David <devnull@localhost>
Date:   Wed Dec 5 10:14:19 2018 +0700

    TCS_DocFactory & Build

[33mcommit 6975d76b35a3338502fb8c085195c3fda2bfe364[m
Author: David <devnull@localhost>
Date:   Wed Dec 5 09:58:51 2018 +0700

    Update MatchAllocation Case AR Receipt - AP Payment

[33mcommit 03fdbefa75e01fa0c7c118df532f366659dff5af[m
Author: David <devnull@localhost>
Date:   Wed Dec 5 09:57:47 2018 +0700

    Implement Set Invoice Fact_Acct.C_BPartner_ID = TCS_ExpenseLine_ID.C_BPartner_ID IF C_InvoiceLine.TCS_ExpenseLine_ID IS NOT NULL

[33mcommit e1bf11f52dffad8d7b1184700ab4ae5dab34b24a[m
Author: David <devnull@localhost>
Date:   Wed Dec 5 09:53:20 2018 +0700

    Update Advance Request & Settlement

[33mcommit 5b5d0851a3034e1c6c52186db4b9ca6d39df2d2c[m
Merge: 2594545 fc63d08
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 30 17:31:16 2018 +0700

    Merge

[33mcommit 25945455a135891a7bb5bfbcdf2b3412cafff1fb[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 30 17:23:54 2018 +0700

    created process for creating payment allocation charge

[33mcommit fc63d08589ecaba2427c5aabd7607c5ca72397e9[m
Merge: f5a88bf 92088c0
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:07:30 2018 +0700

    Merge

[33mcommit f5a88bf841f74d2be499ea1c9bc987c31a4be6ac[m
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:04:36 2018 +0700

    Add Advance Request & Settlement Processes

[33mcommit fbde87df4906d4308a798d8ef4591444371c4ebb[m
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:03:52 2018 +0700

    Update Factory

[33mcommit d065587bd4f0089f58e118a4a30ba181476739de[m
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:03:23 2018 +0700

    Add Validator For Advance Request & Settlement Reverse Invoice / Payment

[33mcommit 27025fd29124eb6a4b2fcfd244beb70e9d6e5a79[m
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:01:31 2018 +0700

    Add Callout Advance Request & Settlement

[33mcommit 61080318be86d4f43d9c62a108afb189f8399ef4[m
Author: David <devnull@localhost>
Date:   Fri Nov 30 14:00:15 2018 +0700

    Add MClass

[33mcommit 92088c0a2e902adca4125053ff9ebeb87ed78341[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Wed Nov 28 15:44:40 2018 +0700

    imported TCS_BankTransfer from slu

[33mcommit e7c32700863e7a7de9d0e5d3c2443bf40a1f3222[m
Author: David <devnull@localhost>
Date:   Tue Nov 27 13:48:34 2018 +0700

    Migrate id.tcs.org.TCS_MAllocationHdt.createMatchAllocation to org.compiere.model.TCS_MAllocationHdt.createMatchAllocation

[33mcommit de2d682808d56085c61040f026dd353cbafc17cf[m
Author: David <devnull@localhost>
Date:   Tue Nov 27 13:32:29 2018 +0700

    TCS_MAllocationHdrValidator fix typo

[33mcommit ceec93231e056d3884b4b9c8958ce436c513275c[m
Author: David <devnull@localhost>
Date:   Mon Nov 26 11:44:31 2018 +0700

    Delete T_MatchAllocation When Reverse AllocationHdr

[33mcommit 241d0b3c81fcca905130a33388dbff236e29c07d[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 17:47:33 2018 +0700

    fixed some loop in TCS_MAllocationHdr and TCS_CreateMatchAllocation

[33mcommit 922185b0a421c669785c31b3f844e147c750e332[m
Merge: 994ab25 6184dee
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 16:11:47 2018 +0700

    Merge

[33mcommit 994ab2566aa07ecdd23db7649516345a92f5fc7c[m
Merge: 05fbe2c 0dde381
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 16:10:40 2018 +0700

    Merge

[33mcommit 05fbe2c9244d34c77fa3d07e53a145dccf869cf4[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 16:10:06 2018 +0700

    makes allocationamt absolute again in TCS_MallocationHdr and TCS_CreateMatchAllocation

[33mcommit 6184dee762596c562785d1511df5f59cb2a5ecfd[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 23 16:00:29 2018 +0700

    update height form

[33mcommit fe3551da73a65ae9ec632309ba9c0243ec899f83[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 14:50:30 2018 +0700

    fixed TCS_CreateMatchAllocation logic

[33mcommit d53e7ebfc9e66d2280d5b2d415d6c8b98de52d46[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 14:50:07 2018 +0700

    fixed TCS_MAllocation hdr t_matchallocation logic

[33mcommit 0dde381191023b0509617d0f4983ce18f2cdad67[m
Author: David <devnull@localhost>
Date:   Fri Nov 23 14:43:25 2018 +0700

    TCS_AllocationReset Delete T_MatchAllocation Prior to AllocationHdr

[33mcommit d5b4ab0ae3ad368d271dd65b6e3353d7c071a722[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Fri Nov 23 13:50:00 2018 +0700

    changed TCS_MAllocationHdr t_matchallocation logic

[33mcommit a463e77bc7f7e9bb178b6386c8e264ad6d88bed5[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Wed Nov 21 19:32:43 2018 +0700

    fixed TCS_CreateMatchAllocation

[33mcommit e44b262a9dd040e70a29405f355148faf92f9864[m
Author: ahmadabdurrahman <devnull@localhost>
Date:   Mon Nov 19 14:17:23 2018 +0700

    implement T_MatchAllocation

[33mcommit d40b80971c37ffc7302e90342c7113e29c6bb873[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 9 15:55:13 2018 +0800

    update hgignore

[33mcommit 77d10073268173c47603490d692ab642c2b2a9a3[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 9 15:55:06 2018 +0800

    approval workflow

[33mcommit 031315c9e1c18d0d194da291650d479ca377c8f2[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 9 15:54:51 2018 +0800

    osgi form factory

[33mcommit 7b81308ea2095b33b9405561cd19dfc363fb6de5[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 9 15:54:39 2018 +0800

    add fragment feature

[33mcommit 902468a78dad6b98b9e8dfd74cd2e4c4c7afd70d[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Nov 9 15:54:15 2018 +0800

    update manifest

[33mcommit 9af777dea3223b82f6ca69c197d403b6ce1f2bdf[m
Author: Phie Albert <devnull@localhost>
Date:   Tue Oct 30 17:33:55 2018 +0700

    enhance bank statement

[33mcommit 0f0883623dd9b4894495deddcabe72923310f01f[m
Author: Phie Albert <devnull@localhost>
Date:   Mon Oct 29 17:25:59 2018 +0700

    add validation when reset allocation

[33mcommit fcf4a327e64dc884a6fff1e7f378dfe327f7ee9c[m
Author: Phie Albert <devnull@localhost>
Date:   Mon Oct 29 17:25:48 2018 +0700

    Payment Allocate Charge development + create allocation for single charge, update hgignore

[33mcommit 2e53c3080edac57c2c4aa74c6a37c96e3553a425[m
Author: Phie Albert <devnull@localhost>
Date:   Mon Oct 29 17:24:53 2018 +0700

    add plugin model

[33mcommit 15294e4ab5b9af85309a7e8d5baae61d7e0aed57[m
Author: Phie Albert <devnull@localhost>
Date:   Mon Oct 29 17:23:08 2018 +0700

    add plugin process

[33mcommit c411bfcb565c69ccd08cef42d36163f21116004b[m
Author: Phie Albert <devnull@localhost>
Date:   Fri Oct 26 11:05:58 2018 +0700

    update bankaccount name

[33mcommit 05bcdfb271d7c32a0623e524e25ebf7bf9359bc1[m
Author: Phie Albert <devnull@localhost>
Date:   Thu Oct 25 17:46:18 2018 +0700

    swap beginnning balance line and reconciled title, description from bank statement line

[33mcommit 5c25f8228e0855db465babaccb5b0a0577da3f12[m
Author: Phie Albert <devnull@localhost>
Date:   Thu Oct 25 14:05:24 2018 +0700

    tcs bankregister report

[33mcommit d2ab13ab69fb716cb5607886bac37c91f431c0df[m
Author: Phie Albert <devnull@localhost>
Date:   Thu Oct 25 14:05:09 2018 +0700

    update hgignore

[33mcommit 6d9596c68feebb0dd6c9da16c79d025337502e62[m
Author: Phie Albert <devnull@localhost>
Date:   Thu Oct 25 14:04:47 2018 +0700

    create feature plugin, fix validator factory

[33mcommit 284f0016452febefdda068999ea9389f48b96abe[m
Author: Phie Albert <devnull@localhost>
Date:   Thu Oct 25 13:26:22 2018 +0700

    create osgi factory, add new dependencies

[33mcommit f64e0f456fed901b2d581a09a28258579c606481[m
Author: edwin <edwin@tobaconsulting.com>
Date:   Thu Oct 25 13:05:43 2018 +0700

    fix renaming error

[33mcommit d2965ce2530a407af7592cf2c60fb82024a97aa6[m
Author: edwin <edwin@tobaconsulting.com>
Date:   Thu Oct 25 12:58:43 2018 +0700

    update hgignore

[33mcommit 8752876e2932542b2bdf7ba259491fd064e0c845[m
Author: edwin <edwin@tobaconsulting.com>
Date:   Thu Oct 25 12:58:18 2018 +0700

    add factories

[33mcommit 2520beada0c708bb114bfa7024bd5541b3cd454e[m
Author: Edwin Ang <edwin@tobaconsulting.com>
Date:   Thu Oct 25 04:09:27 2018 +0000

    Initial commit
